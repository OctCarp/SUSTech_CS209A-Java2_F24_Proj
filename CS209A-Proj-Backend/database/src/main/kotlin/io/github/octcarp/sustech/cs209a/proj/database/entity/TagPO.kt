package io.github.octcarp.sustech.cs209a.proj.database.entity

import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

@TableName("Tags")
data class TagPO(
    @TableId
    val tagId: Int,

    val tagName: String
)