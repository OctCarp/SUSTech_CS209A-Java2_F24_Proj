package io.github.octcarp.sustech.cs209a.proj.crawler.model

import io.github.octcarp.sustech.cs209a.proj.database.dto.AnswerWithDetail
import io.github.octcarp.sustech.cs209a.proj.database.entity.AnswerPO
import io.github.octcarp.sustech.cs209a.proj.database.entity.AnswerQualityPO
import io.github.octcarp.sustech.cs209a.proj.database.entity.QuestionPO
import io.github.octcarp.sustech.cs209a.proj.database.entity.UserPO
import io.github.octcarp.sustech.cs209a.proj.database.enums.QualityType
import java.time.temporal.ChronoUnit

data class AnswerQualityMetrics(
    val responseTimeSeconds: Long = 0,
    val ownerReputation: Int = 0,
    val ownerAcceptRate: Int = 0,
    val answerLength: Int = 0
)

data class AnswerWithDetailDTO(
    internal val answer: AnswerPO,
    private val question: QuestionPO,
    private val owner: UserPO?
) {

    val level: QualityType
    val metrics: AnswerQualityMetrics

    init {
        metrics = defineMetrics()
        level = defineLevel()
    }

    private fun defineMetrics() = AnswerQualityMetrics(
        responseTimeSeconds = question.creationDate.until(answer.creationDate, ChronoUnit.SECONDS),
        answerLength = answer.body.length,
        ownerReputation = owner?.reputation ?: -1,
        ownerAcceptRate = owner?.acceptRate ?: -1
    )

    private fun defineLevel(): QualityType {
        val totalVotes = answer.upVoteCount + answer.downVoteCount
        val upvoteRate = if (totalVotes > 0) answer.upVoteCount.toDouble() / totalVotes else 0.0

        return when {
            metrics.responseTimeSeconds < 20 -> QualityType.POOR
            answer.isAccepted == true || upvoteRate > 0.9 -> QualityType.EXCELLENT
            upvoteRate > 0.7 -> QualityType.GOOD
            upvoteRate > 0.5 -> QualityType.FAIR
            else -> QualityType.POOR
        }
    }
}

fun AnswerWithDetailDTO.toAnswerQualityPO() = AnswerQualityPO(
    answerId = answer.answerId!!,
    qualityLevel = level,
    responseSeconds = metrics.responseTimeSeconds,
    ownerReputation = metrics.ownerReputation,
    ownerAcceptRate = metrics.ownerAcceptRate,
    answerLength = metrics.answerLength
)
