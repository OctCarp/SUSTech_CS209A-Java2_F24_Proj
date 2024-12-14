package io.github.octcarp.sustech.cs209a.proj.crawler.model

import io.github.octcarp.sustech.cs209a.proj.crawler.utils.secondToLocalDateTime
import io.github.octcarp.sustech.cs209a.proj.database.entity.AnswerPO
import kotlinx.serialization.Serializable

@Serializable
data class AnswerDTO(
    val answerId: Long,
    val questionId: Long,
    val owner: BriefUser? = null,

    val body: String,

    val creationDate: Long,
    val lastActivityDate: Long,
    val lastEditDate: Long? = null,

    val score: Int,
    val upVoteCount: Int,
    val downVoteCount: Int,
    val isAccepted: Boolean,

    )

fun AnswerDTO.toPO(): AnswerPO {
    return AnswerPO(
        answerId = answerId.toLong(),
        questionId = questionId.toLong(),
        ownerId = owner?.userId,
        body = body,
        creationDate = creationDate.secondToLocalDateTime(),
        lastActivityDate = lastActivityDate.secondToLocalDateTime(),
        lastEditDate = lastEditDate?.secondToLocalDateTime(),
        score = score,
        upVoteCount = this@toPO.upVoteCount,
        downVoteCount = this@toPO.downVoteCount,
        isAccepted = isAccepted
    )
}