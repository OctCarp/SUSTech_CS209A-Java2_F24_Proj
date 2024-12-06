package io.github.octcarp.sustech.cs209a.proj.crawler.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val userId: Int,

    val userType : String,

    val displayName: String,

    val reputation: Int,

    val badgeCounts: BadgeCounts,

    val creationDate: Long,

    val acceptRate: Int? = 0,
)

@Serializable
data class BadgeCounts(
    val gold: Int,
    val silver: Int,
    val bronze: Int
)