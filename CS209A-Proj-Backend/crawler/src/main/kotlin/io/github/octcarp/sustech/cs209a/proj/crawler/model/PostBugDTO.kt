package io.github.octcarp.sustech.cs209a.proj.crawler.model

import io.github.octcarp.sustech.cs209a.proj.database.enums.PostType

enum class PostTypeDTO(
    private val value: Int
) {
    QUESTION(1),
    ANSWER(2),
    COMMENT(3);

    fun getValue(): Int {
        return value
    }
}

enum class BugTypeDTO(
    private val value: Int
) {
    ERROR(1),
    EXCEPTION(2);

    fun getValue(): Int {
        return value
    }
}

data class PostBugDTO(

    val postId: Long,

    val postType: PostType,

    val bugName: String,

    val bugFreq: Int,

    val bugDescription: String? = null,
)
