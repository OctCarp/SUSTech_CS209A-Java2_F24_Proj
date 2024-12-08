package io.github.octcarp.sustech.cs209a.proj.database.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import io.github.octcarp.sustech.cs209a.proj.database.enums.PostType

@TableName("Post_Topics")
data class PostTopicPO(
    @TableField
    val postId: Long,

    @TableField(value = "post_type")
    val postType: PostType,

    @TableField
    val topicId: Long,
)
