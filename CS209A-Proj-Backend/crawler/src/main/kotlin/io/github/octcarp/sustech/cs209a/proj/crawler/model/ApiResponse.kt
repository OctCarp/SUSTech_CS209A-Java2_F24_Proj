package io.github.octcarp.sustech.cs209a.proj.crawler.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val items: List<T>,

    val hasMore: Boolean,

    val quotaMax: Int,

    val quotaRemaining: Int,
)