package io.github.octcarp.sustech.cs209a.proj.database.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import io.github.octcarp.sustech.cs209a.proj.database.dto.AnswerWithDetail
import io.github.octcarp.sustech.cs209a.proj.database.entity.AnswerQualityPO
import io.github.octcarp.sustech.cs209a.proj.database.mapper.AnswerQualityMapper
import io.github.octcarp.sustech.cs209a.proj.database.service.AnswerQualityService
import org.springframework.stereotype.Service

@Service
class AnswerQualityServiceImpl : ServiceImpl<AnswerQualityMapper, AnswerQualityPO>(), AnswerQualityService {

    override fun selectAllAnswerWithDetail(): List<AnswerWithDetail> = baseMapper.selectAllAnswerWithDetail()

}