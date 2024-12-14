package io.github.octcarp.sustech.cs209a.proj.crawler.model

import io.github.octcarp.sustech.cs209a.proj.crawler.utils.secondToLocalDateTime
import io.github.octcarp.sustech.cs209a.proj.database.entity.CommentPO
import kotlinx.serialization.Serializable

@Serializable
data class CommentDTO(
    val commentId: Long,
    val postId: Long,
    val owner: BriefUser? = null,

    val body: String,

    val creationDate: Long,

    val score: Int,
)

fun CommentDTO.toPO(): CommentPO {
    return CommentPO(
        commentId = commentId,
        postId = postId,
        ownerId = owner?.userId,
        body = body,
        creationDate = creationDate.secondToLocalDateTime(),
        score = score,
    )
}