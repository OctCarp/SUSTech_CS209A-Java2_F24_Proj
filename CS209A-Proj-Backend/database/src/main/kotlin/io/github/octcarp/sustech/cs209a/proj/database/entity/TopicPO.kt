package io.github.octcarp.sustech.cs209a.proj.database.entity

import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

@TableName("Topics")
data class TopicPO(
    @TableId
    val topicId: Long,
    val topicName: String,
    val frequency: Int = 0,
)