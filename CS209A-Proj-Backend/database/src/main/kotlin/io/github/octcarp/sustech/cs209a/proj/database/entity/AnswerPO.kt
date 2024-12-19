package io.github.octcarp.sustech.cs209a.proj.database.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.time.LocalDateTime

@TableName("Answers")
data class AnswerPO(
    @TableId(type = IdType.INPUT)
    val answerId: Long? = null,
    val questionId: Long,
    val ownerId: Long?,

    val body: String,

    @TableField(typeHandler = org.apache.ibatis.type.LocalDateTimeTypeHandler::class)
    val creationDate: LocalDateTime,
    @TableField(typeHandler = org.apache.ibatis.type.LocalDateTimeTypeHandler::class)
    val lastActivityDate: LocalDateTime,
    @TableField(typeHandler = org.apache.ibatis.type.LocalDateTimeTypeHandler::class)
    val lastEditDate: LocalDateTime?,

    val score: Int,
    val upVoteCount: Int,
    val downVoteCount: Int,
    val isAccepted: Boolean,
)