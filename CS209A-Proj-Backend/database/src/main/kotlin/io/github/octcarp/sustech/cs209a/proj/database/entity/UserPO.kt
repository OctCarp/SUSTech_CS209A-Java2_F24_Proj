package io.github.octcarp.sustech.cs209a.proj.database.entity

import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.time.LocalDateTime

@TableName("Users")
data class UserPO(
    @TableId
    val userId: Long,
    val displayName: String,
    val creationDate: LocalDateTime,

    val reputation: Int,
    val acceptRate: Int? = null,
    val upVoteCount: Int,
    val downVoteCount: Int,
)