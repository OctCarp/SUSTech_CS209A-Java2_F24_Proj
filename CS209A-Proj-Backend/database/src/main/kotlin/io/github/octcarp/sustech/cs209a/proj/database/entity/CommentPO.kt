package io.github.octcarp.sustech.cs209a.proj.database.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.time.LocalDateTime

@TableName("Comments")
data class CommentPO(
    @TableId(type = IdType.INPUT)
    val commentId: Long? = null,
    val postId: Long,
    val ownerId: Long?,

    val body: String?,

    @TableField(typeHandler = org.apache.ibatis.type.LocalDateTimeTypeHandler::class)
    val creationDate: LocalDateTime,

    val score: Int,
)