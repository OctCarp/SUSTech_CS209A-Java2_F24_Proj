package io.github.octcarp.sustech.cs209a.proj.database.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import io.github.octcarp.sustech.cs209a.proj.database.enums.QualityType

@TableName("Answers_Quality")
data class AnswerQualityPO(
    @TableId(type = IdType.INPUT)
    val answerId: Long? = null,
    val qualityLevel: QualityType,
    val responseSeconds: Long,
    val ownerReputation: Int?,
    val ownerAcceptRate: Int?,
    val answerLength: Int
)
