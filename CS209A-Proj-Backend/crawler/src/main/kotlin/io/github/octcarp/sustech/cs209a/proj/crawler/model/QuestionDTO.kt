package io.github.octcarp.sustech.cs209a.proj.crawler.model

import io.github.octcarp.sustech.cs209a.proj.crawler.utils.secondToLocalDateTime
import io.github.octcarp.sustech.cs209a.proj.database.entity.QuestionPO
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDTO(
    val questionId: Long,
    val owner: BriefUser? = null,

    val tags: List<String>,

    val title: String,
    val body: String,

    val creationDate: Long,
    val lastActivityDate: Long,
    val lastEditDate: Long? = null,

    val acceptedAnswerId: Long? = null,

    val score: Int,
    val viewCount: Int,
    val upVoteCount: Int,
    val downVoteCount: Int,
    val favoriteCount: Int,
)

fun QuestionDTO.toPO(): QuestionPO {
    return QuestionPO(
        questionId = questionId,
        ownerId = owner?.userId,
        title = title,
        body = body,
        creationDate = creationDate.secondToLocalDateTime(),
        lastActivityDate = lastActivityDate.secondToLocalDateTime(),
        lastEditDate = lastEditDate?.secondToLocalDateTime(),
        acceptedAnswerId = acceptedAnswerId,
        score = score,
        viewCount = viewCount,
        upVoteCount = upVoteCount,
        downVoteCount = downVoteCount,
        favoriteCount = favoriteCount
    )
}