package io.github.octcarp.sustech.cs209a.proj.crawler.fetcher

import com.google.common.util.concurrent.RateLimiter
import io.github.octcarp.sustech.cs209a.proj.crawler.config.CrawlerConfig
import io.github.octcarp.sustech.cs209a.proj.crawler.config.JsonType
import io.github.octcarp.sustech.cs209a.proj.crawler.model.AnswerDTO
import io.github.octcarp.sustech.cs209a.proj.crawler.model.ApiResponse
import io.github.octcarp.sustech.cs209a.proj.crawler.model.CommentDTO
import io.github.octcarp.sustech.cs209a.proj.crawler.model.QuestionDTO
import io.github.octcarp.sustech.cs209a.proj.crawler.model.UserDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import kotlin.collections.chunked


val rateLimiter = RateLimiter.create(0.03)
val client = OkHttpClient.Builder()
    .readTimeout(30, TimeUnit.SECONDS)
    .addInterceptor { chain ->
        rateLimiter.acquire()
        chain.proceed(chain.request())
    }
    .build()

val json = JsonType.json

val commonParams =
    "site=${CrawlerConfig.SITE}&key=${CrawlerConfig.API_KEY}&filter=${CrawlerConfig.FILTER}"

private fun getStringFromUrl(url: String): String? {
    val request = Request.Builder().url(url).build()
    println("A request to has been made at seconds: ${System.currentTimeMillis() / 1000}")
    client.newCall(request).execute().use { response ->
        if (response.isSuccessful) {
            return response.body?.string()
        } else {
            error("Request: ${url}\nFailed: ${response.message}")
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

        val params = "${commonParams}&order=desc&sort=votes" +
                "&tag=${CrawlerConfig.TAG}&page=${page}&pagesize=${pageSize}"

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
    suspend fun fetchQuestionChunk(chunk: List<Long>): List<QuestionDTO> {
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
    suspend fun fetchAnswerChunk(chunk: List<Long>): List<AnswerDTO> {
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

    return runBlocking {
        questionIds.chunked(50)
            .map { chunk ->
                async(Dispatchers.IO) {
                    fetchAnswerChunk(chunk)
                }
            }
            .awaitAll()
            .flatten()
    }
}

fun fetchCommentsByPostIds(
    postType: String,
    postIds: List<Long>
): List<CommentDTO> {
    suspend fun fetchCommentChunk(chunk: List<Long>): List<CommentDTO> {
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

}

fun fetchUsersByIds(userIds: List<Long>): List<UserDTO> {
    suspend fun fetchUserChunk(chunk: List<Long>): List<UserDTO> {
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
}
