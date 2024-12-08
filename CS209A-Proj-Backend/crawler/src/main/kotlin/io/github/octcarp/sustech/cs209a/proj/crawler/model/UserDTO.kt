package io.github.octcarp.sustech.cs209a.proj.crawler.model

import io.github.octcarp.sustech.cs209a.proj.crawler.utils.toLocalDateTime
import io.github.octcarp.sustech.cs209a.proj.database.entity.UserPO
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val userId: Long,
    val displayName: String,
    val creationDate: Long,

    val questionCount : Int,
    val answerCount : Int,
    val acceptRate: Int? = null,

    val reputation: Int,
    val upVoteCount: Int,
    val downVoteCount: Int,

    val badgeCounts: BadgeCounts
)

@Serializable
data class BadgeCounts(
    val gold: Int,
    val silver: Int,
    val bronze: Int
)

fun UserDTO.toPO(): UserPO {
    return UserPO(
        userId = userId,
        displayName = displayName,
        creationDate = creationDate.toLocalDateTime(),
        reputation = reputation,
        acceptRate = acceptRate,
        upVoteCount = this@toPO.upVoteCount,
        downVoteCount = this@toPO.downVoteCount
    )
}