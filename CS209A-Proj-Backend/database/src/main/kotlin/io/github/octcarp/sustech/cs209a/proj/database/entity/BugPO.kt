package io.github.octcarp.sustech.cs209a.proj.database.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import io.github.octcarp.sustech.cs209a.proj.database.enums.BugType

@TableName("Bugs")
data class BugPO(
    @TableId
    val bugId: Long? = null,
    val bugName: String,

    @TableField(value = "bug_type")
    val bugType: BugType,
    val bugFrequency: Long,

    val bugDesc: String?,
)