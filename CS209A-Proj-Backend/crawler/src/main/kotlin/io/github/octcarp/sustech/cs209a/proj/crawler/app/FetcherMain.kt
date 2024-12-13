package io.github.octcarp.sustech.cs209a.proj.crawler.app

import io.github.octcarp.sustech.cs209a.proj.crawler.config.JsonType
import io.github.octcarp.sustech.cs209a.proj.crawler.model.QuestionDTO
import io.github.octcarp.sustech.cs209a.proj.crawler.fetcher.fetchAnswersByQuestions
import io.github.octcarp.sustech.cs209a.proj.crawler.fetcher.fetchCommentsByPostIds
import io.github.octcarp.sustech.cs209a.proj.crawler.fetcher.fetchQuestions
import io.github.octcarp.sustech.cs209a.proj.crawler.fetcher.fetchQuestionsByIds
import io.github.octcarp.sustech.cs209a.proj.crawler.fetcher.fetchUsersByIds
import io.github.octcarp.sustech.cs209a.proj.crawler.fetcher.saveUsersResume
import io.github.octcarp.sustech.cs209a.proj.crawler.model.AnswerDTO
import io.github.octcarp.sustech.cs209a.proj.crawler.model.CommentDTO
import io.github.octcarp.sustech.cs209a.proj.crawler.model.UserDTO
import io.github.octcarp.sustech.cs209a.proj.crawler.utils.loadAnswerJson
import io.github.octcarp.sustech.cs209a.proj.crawler.utils.loadCommentJson
import io.github.octcarp.sustech.cs209a.proj.crawler.utils.loadQuestionJson
import io.github.octcarp.sustech.cs209a.proj.crawler.utils.loadUserJson
import io.github.octcarp.sustech.cs209a.proj.crawler.utils.saveToFile
import kotlinx.serialization.encodeToString
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.mapNotNull


fun main(args: Array<String>) {
    CrawlerMain.execute()
}

object CrawlerMain {
    const val QUES_NEW: Boolean = false
    const val QUES_REFRESH: Boolean = false

    const val ANSWER_NEW: Boolean = false

    const val COMMENT_NEW: Boolean = false

    const val USER_NEW: Boolean = true

    val json = JsonType.json
    val saveDir = "./data/${LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd_HH-mm-ss"))}"

    fun execute() {
        val questionDTOList = if (QUES_NEW) saveNewQuestions() else loadQuestionJson()
        val questionIds = questionDTOList.map { it.questionId }

        if (QUES_REFRESH) {
            saveRefreshQuestions(questionIds)
        }

        val answerDTOList = if (ANSWER_NEW || QUES_NEW) {
            saveNewAnswers(questionIds)
        } else {
            loadAnswerJson()
        }

        val commentDTOList = if (COMMENT_NEW || QUES_NEW) {
            saveNewComments(
                questionIds = questionIds,
                answerIds = answerDTOList.map { it.answerId }
            )
        } else {
            loadCommentJson()
        }

        val userDTOList = if (USER_NEW || QUES_NEW) {
            val userIds = (questionDTOList.mapNotNull { it.owner?.userId }
                    + answerDTOList.mapNotNull { it.owner?.userId }
                    + commentDTOList.mapNotNull { it.owner?.userId }).distinct()
            saveNewUsers(userIds)
        } else {
            loadUserJson()
        }
    }

    private fun saveNewQuestions(): List<QuestionDTO> {
        val questions: List<QuestionDTO> =
            fetchQuestions(maxQuestions = 2000)
        saveToFile(
            "${saveDir}/question.json",
            json.encodeToString(questions)
        )

        return questions
    }

    private fun saveRefreshQuestions(questionIds: List<Long>) {
        val questions = fetchQuestionsByIds(questionIds)
        saveToFile(
            "${saveDir}/question.json",
            json.encodeToString(questions)
        )
    }

    private fun saveNewAnswers(questionIds: List<Long>): List<AnswerDTO> {
        val answers = fetchAnswersByQuestions(questionIds)
        saveToFile(
            "${saveDir}/answer.json",
            json.encodeToString(answers)
        )
        return answers
    }

    private fun saveNewComments(
        questionIds: List<Long>,
        answerIds: List<Long>
    ): List<CommentDTO> {
        val questionComments = fetchCommentsByPostIds("questions", questionIds)
        val answerComments = fetchCommentsByPostIds("answers", answerIds)

        val comments = questionComments + answerComments
        saveToFile(
            "${saveDir}/comment.json",
            json.encodeToString(comments)
        )
        return comments
    }

    private fun saveNewUsers(userIds: List<Long>): List<UserDTO> {
        var users: List<UserDTO>
        try {
            users = saveUsersResume(userIds)
            saveToFile(
                "${saveDir}/user.json",
                json.encodeToString(users)
            )
        } catch (_: Exception) {
            println("No users.json found, start from 0")
            users = fetchUsersByIds(userIds, emptyList())
            saveToFile(
                "${saveDir}/user.json",
                json.encodeToString(users)
            )
        }

        return users
    }
}