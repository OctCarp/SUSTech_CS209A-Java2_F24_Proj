package io.github.octcarp.sustech.cs209a.proj.database.enums

import com.baomidou.mybatisplus.annotation.IEnum

enum class QualityType(
    private val value: Int,
    private val desc: String
) : IEnum<Int> {
    POOR(1, "Poor"),
    FAIR(2, "Fair"),
    GOOD(3, "Good"),
    EXCELLENT(4, "Excellent");

    override fun getValue(): Int {
        return value
    }
}