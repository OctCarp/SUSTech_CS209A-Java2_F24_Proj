package io.github.octcarp.sustech.cs209a.proj.database.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.time.LocalDateTime

@TableName("Questions")
data class QuestionPO(
    @TableId(type = IdType.INPUT)
    val questionId: Long? = null,
    val ownerId: Long?,

    val title: String,
    val body: String,

    val creationDate: LocalDateTime,
    val lastActivityDate: LocalDateTime,
    val lastEditDate: LocalDateTime?,

    val acceptedAnswerId: Long?,

    val score: Int,
    val viewCount: Int,
    val upVoteCount: Int,
    val downVoteCount: Int,
    val favoriteCount: Int
)