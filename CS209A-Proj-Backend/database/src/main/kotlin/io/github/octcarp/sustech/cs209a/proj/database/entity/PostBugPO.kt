package io.github.octcarp.sustech.cs209a.proj.database.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import io.github.octcarp.sustech.cs209a.proj.database.enums.PostType

@TableName("PostBugs")
data class PostBugPO(
    @TableField
    val postId: Long,

    @TableField(value = "post_type")
    val postType: PostType,

    @TableField
    val bugId: Long,

    @TableField
    val frequency: Int = 0
)
