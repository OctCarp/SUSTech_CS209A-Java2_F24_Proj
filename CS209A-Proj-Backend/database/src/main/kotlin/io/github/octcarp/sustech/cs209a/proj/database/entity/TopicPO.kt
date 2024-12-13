package io.github.octcarp.sustech.cs209a.proj.database.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

@TableName("Topics")
data class TopicPO(
    @TableId(type = IdType.AUTO)
    val topicId: Long? = null,

    val topicName: String,
    val frequency: Long = 0,
)