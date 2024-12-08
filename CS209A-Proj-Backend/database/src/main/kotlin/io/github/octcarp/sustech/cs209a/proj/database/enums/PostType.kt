package io.github.octcarp.sustech.cs209a.proj.database.enums

import com.baomidou.mybatisplus.annotation.IEnum

enum class PostType(
    private val value: Int,
    private val desc: String
) : IEnum<Int> {
    QUESTION(1, "question post"),
    ANSWER(2, "answer post"),
    ARTICLE(3, "article post"),
    COMMENT(4, "comment post");

    override fun getValue(): Int {
        return value
    }
}