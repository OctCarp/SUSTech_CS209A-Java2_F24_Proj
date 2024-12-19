package io.github.octcarp.sustech.cs209a.proj.database.service

import com.baomidou.mybatisplus.extension.service.IService
import io.github.octcarp.sustech.cs209a.proj.database.dto.AnswerWithDetail
import io.github.octcarp.sustech.cs209a.proj.database.entity.AnswerQualityPO

interface AnswerQualityService : IService<AnswerQualityPO> {
    fun selectAllAnswerWithDetail(): List<AnswerWithDetail>
}