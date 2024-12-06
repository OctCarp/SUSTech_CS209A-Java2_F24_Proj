package io.github.octcarp.sustech.cs209a.proj.crawler.model

import kotlinx.serialization.Serializable

@Serializable
data class QuestionDTO(
    val questionId: Int,

    val title: String,

    val body: String,

    val tags: List<String>,

    val creationDate: Long,

    val lastActivityDate: Long,

    val owner: BriefUser? = null,

    val score: Int?,

    val viewCount: Int,

    val isAnswered: Boolean,

    val answerCount: Int,

    val acceptedAnswerId: Int? = null
)