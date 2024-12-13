package io.github.octcarp.sustech.cs209a.proj.database.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import io.github.octcarp.sustech.cs209a.proj.database.enums.PostType

@TableName("Post_Bugs")
data class PostBugPO(
    @TableId(type = IdType.AUTO)
    val postBugId: Long? = null,

    val postId: Long? = null,

    @TableField(value = "post_type")
    val postType: PostType,

    val bugId: Long,

    val frequency: Int
)
