package io.github.octcarp.sustech.cs209a.proj.database.entity

import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.time.LocalDateTime

@TableName("Comments")
data class CommentPO(
    @TableId
    val commentId: Long,
    val postId: Long,
    val ownerId: Long?,

    val body: String?,

    val creationDate: LocalDateTime,

    val score: Int,
)