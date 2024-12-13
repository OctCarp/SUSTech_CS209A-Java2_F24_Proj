package io.github.octcarp.sustech.cs209a.proj.crawler.fetcher

import com.google.common.util.concurrent.RateLimiter
import io.github.octcarp.sustech.cs209a.proj.crawler.app.CrawlerMain
import io.github.octcarp.sustech.cs209a.proj.crawler.app.CrawlerMain.saveDir
import io.github.octcarp.sustech.cs209a.proj.crawler.config.CrawlerConfig
import io.github.octcarp.sustech.cs209a.proj.crawler.config.JsonType
import io.github.octcarp.sustech.cs209a.proj.crawler.model.AnswerDTO
import io.github.octcarp.sustech.cs209a.proj.crawler.model.ApiResponse
import io.github.octcarp.sustech.cs209a.proj.crawler.model.CommentDTO
import io.github.octcarp.sustech.cs209a.proj.crawler.model.QuestionDTO
import io.github.octcarp.sustech.cs209a.proj.crawler.model.UserDTO
import io.github.octcarp.sustech.cs209a.proj.crawler.utils.loadUserResumeJson
import io.github.octcarp.sustech.cs209a.proj.crawler.utils.saveToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit
import kotlin.collections.chunked

const val USE_COROUTINE: Boolean = false

val rateLimiter: RateLimiter = RateLimiter.create(1.0)
val client = OkHttpClient.Builder()
    .readTimeout(30, TimeUnit.SECONDS)
    .addInterceptor { chain ->
        rateLimiter.acquire()
        chain.proceed(chain.request())
    }
    .build()

var backOffCoe: Double = 4.0

var noBackOffCount = 0

var lastBackOffCount = 1


val json = JsonType.json

val commonParams =
    "site=${CrawlerConfig.SITE}&key=${CrawlerConfig.API_KEY}&filter=${CrawlerConfig.FILTER}"

private fun getStringFromUrl(url: String): String? {
    val request = Request.Builder().url(url).build()
    val maxRetries = 4
    var retryCount = 0
    println("A request to has been made at seconds: ${System.currentTimeMillis() / 1000}")
    while (retryCount < maxRetries) {
        try {
            client.newCall(request).execute().use { response ->
                when {
                    response.isSuccessful -> {
                        ++noBackOffCount
                        return response.body?.string()
                    }

                    else -> throw IOException("request failed: $url\n message: ${response.message}")
                }
            }
        } catch (e: Exception) {
            ++retryCount
            backOffCoe = if (noBackOffCount / lastBackOffCount < 0.9 || noBackOffCount < 5) {
                (backOffCoe * 2).coerceAtMost(8.0)
            } else {
                (backOffCoe / 2).coerceAtLeast(2.0)
            }

            if (noBackOffCount > 0) {
                lastBackOffCount = noBackOffCount
                noBackOffCount = 0
            }
            println("Exception: $e")
            println("Retry after $backOffCoe minutes")
            sleep((61_000 * backOffCoe).toLong())
        }
    }
    return null
}

fun fetchQuestions(
    pageSize: Int = 100,
    maxQuestions: Int = 1000
): List<QuestionDTO> {
    val questionDTOS = mutableListOf<QuestionDTO>()
    var page = 1

    val api = "${CrawlerConfig.BASE_URL}/questions"
    while (questionDTOS.size < maxQuestions) {

        val params = "${commonParams}&order=desc&sort=activity" +
                "&tagged=${CrawlerConfig.TAG}&page=${page}&pagesize=${pageSize}"

        val url = "${api}?${params}"
        val response = getStringFromUrl(url)

        if (response != null) {
            try {
                val questionPage = json.decodeFromString<ApiResponse<QuestionDTO>>(response)
                questionDTOS.addAll(questionPage.items)
                if (!questionPage.hasMore) {
                    break
                }
            } catch (_: Exception) {
                error("Failed to decode response: \n$response")
                break
            }
        } else {
            error("Failed to fetch questions.")
            break
        }

        ++page
    }

    return questionDTOS.take(maxQuestions)
}

fun fetchQuestionsByIds(questionIds: List<Long>): List<QuestionDTO> {
    fun fetchQuestionChunk(chunk: List<Long>): List<QuestionDTO> {
        val questionDTOList = mutableListOf<QuestionDTO>()

        val ids = chunk.joinToString(";")

        val api = "${CrawlerConfig.BASE_URL}/questions/${ids}"
        val params = commonParams

        var page = 1
        var hasMore = true
        while (hasMore) {
            val url = "${api}?${params}&page=${page}"

            val response = getStringFromUrl(url)
            response?.let {
                val answerPage = json.decodeFromString<ApiResponse<QuestionDTO>>(it)
                questionDTOList.addAll(answerPage.items)

                hasMore = answerPage.hasMore
                if (hasMore) {
                    ++page
                }
            } ?: run {
                hasMore = false
            }
        }

        return questionDTOList
    }

    return runBlocking {
        questionIds.chunked(100)
            .map { chunk ->
                async(Dispatchers.IO) {
                    fetchQuestionChunk(chunk)
                }
            }
            .awaitAll()
            .flatten()
    }
}


fun fetchAnswersByQuestions(questionIds: List<Long>): List<AnswerDTO> {
    fun fetchAnswerChunk(chunk: List<Long>): List<AnswerDTO> {
        val answerDTOList = mutableListOf<AnswerDTO>()

        val ids = chunk.joinToString(";")

        val api = "${CrawlerConfig.BASE_URL}/questions/${ids}/answers"
        val params = commonParams

        var page = 1
        var hasMore = true
        while (hasMore) {
            val url = "${api}?${params}&page=${page}"

            val response = getStringFromUrl(url)
            response?.let {
                val answerPage = json.decodeFromString<ApiResponse<AnswerDTO>>(it)
                answerDTOList.addAll(answerPage.items)

                hasMore = answerPage.hasMore

                if (hasMore) {
                    ++page
                }
            } ?: run {
                hasMore = false
            }
        }

        return answerDTOList
    }

    if (USE_COROUTINE) {
        return runBlocking {
            questionIds.chunked(100)
                .map { chunk ->
                    async(Dispatchers.IO) {
                        fetchAnswerChunk(chunk)
                    }
                }
                .awaitAll()
                .flatten()
        }
    } else {
        return questionIds.chunked(100)
            .map { chunk ->
                fetchAnswerChunk(chunk)
            }
            .flatten()
    }
}

fun fetchCommentsByPostIds(
    postType: String,
    postIds: List<Long>
): List<CommentDTO> {
    fun fetchCommentChunk(chunk: List<Long>): List<CommentDTO> {
        val commentDTOList = mutableListOf<CommentDTO>()

        val ids = chunk.joinToString(";")

        val api = "${CrawlerConfig.BASE_URL}/${postType}/${ids}/comments";
        val params = commonParams

        var page = 1
        var hasMore = true
        while (hasMore) {
            val url = "${api}?${params}&page=${page}"
            val response = getStringFromUrl(url)
            response?.let {
                val answerPage = json.decodeFromString<ApiResponse<CommentDTO>>(it)
                commentDTOList.addAll(answerPage.items)

                hasMore = answerPage.hasMore
                if (hasMore) {
                    ++page
                }
            } ?: run {
                hasMore = false
            }
        }

        return commentDTOList
    }

    if (USE_COROUTINE) {
        return runBlocking {
            postIds.chunked(100)
                .map { chunk ->
                    async(Dispatchers.IO) {
                        fetchCommentChunk(chunk)
                    }
                }
                .awaitAll()
                .flatten()
        }
    } else {
        return postIds.chunked(100)
            .map { chunk ->
                fetchCommentChunk(chunk)
            }
            .flatten()
    }

}

fun fetchUsersByIds(userIds: List<Long>, alreadyUser: List<UserDTO>): List<UserDTO> {
    fun fetchUserChunk(chunk: List<Long>): List<UserDTO> {
        val userDTOList = mutableListOf<UserDTO>()

        val ids = chunk.joinToString(";")

        val api = "${CrawlerConfig.BASE_URL}/users/${ids}"
        val params = commonParams

        var page = 1
        var hasMore = true
        while (hasMore) {
            val url = "${api}?${params}&page=${page}"

            val response = getStringFromUrl(url)
            response?.let {
                val answerPage = json.decodeFromString<ApiResponse<UserDTO>>(it)
                userDTOList.addAll(answerPage.items)

                hasMore = answerPage.hasMore
                if (hasMore) {
                    ++page
                }
            } ?: run {
                hasMore = false
            }
        }

        return userDTOList
    }

    val allUsers = alreadyUser.toMutableList()
    val alreadyUserIds = allUsers.map { it.userId }
    val newIds = userIds.filter { it !in alreadyUserIds }

    if (USE_COROUTINE) {
        return runBlocking {
            userIds.chunked(100)
                .map { chunk ->
                    async(Dispatchers.IO) {
                        fetchUserChunk(chunk)
                    }
                }
                .awaitAll()
                .flatten()
        }
    } else {
        return newIds.chunked(100)
            .map { chunk ->
                val users = fetchUserChunk(chunk)
                allUsers.addAll(users)
                saveToFile(
                    "${saveDir}/user_${System.currentTimeMillis() / 1000}.json",
                    json.encodeToString(allUsers)
                )
                users
            }
            .flatten()
    }
}

fun saveUsersResume(userIds: List<Long>): List<UserDTO> {
    val alreadyUsers: List<UserDTO> = loadUserResumeJson()

    val users = fetchUsersByIds(userIds, alreadyUsers)
    return users
}