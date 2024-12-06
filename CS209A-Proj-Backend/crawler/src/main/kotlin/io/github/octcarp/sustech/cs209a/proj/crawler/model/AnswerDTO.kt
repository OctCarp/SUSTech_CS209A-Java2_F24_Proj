package io.github.octcarp.sustech.cs209a.proj.crawler.model

import kotlinx.serialization.Serializable

@Serializable
data class AnswerDTO(
    val answerId: Int,

    val questionId: Int,

    val body: String?,

    val score: Int?,

    val creationDate: Long,

    val owner: BriefUser?,

    val isAccepted: Boolean,

)