package io.github.octcarp.sustech.cs209a.proj.crawler.fetcher

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

val client = OkHttpClient.Builder()
    .readTimeout(30, TimeUnit.SECONDS)
    .build()

val json = JsonType.json

val commonParams =
    "site=${CrawlerConfig.SITE}&key=${CrawlerConfig.API_KEY}&filter=!9_bDE(fI5"

private fun getStringFromUrl(url: String): String? {
    val request = Request.Builder().url(url).build()
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

        val params = "${commonParams}&order=desc&sort=activity" +
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


fun fetchAnswersByQuestions(questionIds: List<Int>): List<AnswerDTO> {
    suspend fun fetchAnswerChunk(chunk: List<Int>): List<AnswerDTO> {
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
        questionIds.chunked(100)
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
    postIds: List<Int>
): List<CommentDTO> {
    suspend fun fetchCommentChunk(chunk: List<Int>): List<CommentDTO> {
        val commentDTOList = mutableListOf<CommentDTO>()

        val ids = chunk.joinToString(";")

        val api = "${CrawlerConfig.BASE_URL}/${postType}/${ids}/comments";
        val params = "site=${CrawlerConfig.SITE}&key=${CrawlerConfig.API_KEY}&filter=withbody"

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

fun fetchUsersByIds(userIds: List<Int>): List<UserDTO> {
    suspend fun fetchUserChunk(chunk: List<Int>): List<UserDTO> {
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
