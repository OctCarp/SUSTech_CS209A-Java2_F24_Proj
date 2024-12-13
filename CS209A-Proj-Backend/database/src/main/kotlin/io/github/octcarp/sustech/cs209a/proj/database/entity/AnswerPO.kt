package io.github.octcarp.sustech.cs209a.proj.database.entity

import com.baomidou.mybatisplus.annotation.IdType
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

    val creationDate: LocalDateTime,
    val lastActivityDate: LocalDateTime,
    val lastEditDate: LocalDateTime?,

    val score: Int,
    val upVoteCount: Int,
    val downVoteCount: Int,
    val isAccepted: Boolean,
)