package io.github.octcarp.sustech.cs209a.proj.database.enums

import com.baomidou.mybatisplus.annotation.IEnum

enum class BugType(
    private val value: Int,
    private val description: String
) : IEnum<Int> {
    ERROR(1, "Fatal errors"),
    EXCEPTION(2, "Exceptions");

    override fun getValue(): Int {
        return value
    }
}