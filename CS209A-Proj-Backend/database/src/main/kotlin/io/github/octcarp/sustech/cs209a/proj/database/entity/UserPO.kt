package io.github.octcarp.sustech.cs209a.proj.database.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.time.LocalDateTime

@TableName("Users")
data class UserPO(
    @TableId(type = IdType.INPUT)
    val userId: Long? = null,
    val displayName: String,
    val creationDate: LocalDateTime,

    val reputation: Int,
    val acceptRate: Int? = null,
    val upVoteCount: Int,
    val downVoteCount: Int,
)