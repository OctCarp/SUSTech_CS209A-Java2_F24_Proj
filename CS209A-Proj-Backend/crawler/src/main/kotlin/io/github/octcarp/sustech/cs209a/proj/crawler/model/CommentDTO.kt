package io.github.octcarp.sustech.cs209a.proj.crawler.model

import kotlinx.serialization.Serializable

@Serializable
data class CommentDTO(
    val commentId: Int,

    val postId: Int,

    val body: String,

    val creationDate: Long,

    val owner: BriefUser?,

    val score: Int
)
