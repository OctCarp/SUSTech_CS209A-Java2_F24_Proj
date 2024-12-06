package io.github.octcarp.sustech.cs209a.proj.crawler.utils

import io.github.octcarp.sustech.cs209a.proj.crawler.config.CrawlerConfig
import io.github.octcarp.sustech.cs209a.proj.crawler.config.JsonType
import io.github.octcarp.sustech.cs209a.proj.crawler.model.AnswerDTO
import io.github.octcarp.sustech.cs209a.proj.crawler.model.CommentDTO
import io.github.octcarp.sustech.cs209a.proj.crawler.model.QuestionDTO
import io.github.octcarp.sustech.cs209a.proj.crawler.model.UserDTO
import java.io.File

val json = JsonType.json

fun saveToFile(filename: String, content: String) {
    val file = File(filename)
    file.parentFile?.let {
        if (!it.exists()) {
            it.mkdirs()
        }
    }

    file.writeText(content)
    println("Write a string to $filename")
}

private fun loadFromFile(filename: String): String {
    val pathStr = "${CrawlerConfig.JSON_DIR}/$filename"
    val path = CrawlerConfig::class.java.getResource(pathStr)?.toURI()?.path
        ?: throw Exception("File not found: $filename")
    return File(path).takeIf { it.exists() }?.readText()
        ?: throw Exception("File not found: $path")
}

fun loadQuestionJson(): List<QuestionDTO> {
    val questionJson = loadFromFile("question.json")
    return json.decodeFromString(questionJson)
}

fun loadAnswerJson(): List<AnswerDTO> {
    val answerJson = loadFromFile("answer.json")
    return json.decodeFromString(answerJson)
}

fun loadCommentJson(): List<CommentDTO> {
    val commentJson = loadFromFile("comment.json")
    return json.decodeFromString(commentJson)
}

fun loadUserJson(): List<UserDTO> {
    val userJson = loadFromFile("user.json")
    return json.decodeFromString(userJson)
}

