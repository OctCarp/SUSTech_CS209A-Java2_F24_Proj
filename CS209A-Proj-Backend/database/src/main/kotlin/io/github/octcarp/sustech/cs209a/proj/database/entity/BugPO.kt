package io.github.octcarp.sustech.cs209a.proj.database.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import io.github.octcarp.sustech.cs209a.proj.database.enums.BugType

@TableName("Bugs")
data class BugPO(
    @TableId
    val bugId: Long,
    val bugName: String,

    @TableField(value = "bug_type")
    val bugType: BugType,
    val bugFrequency: Int = 0,

    val bugDesc: String?,
)