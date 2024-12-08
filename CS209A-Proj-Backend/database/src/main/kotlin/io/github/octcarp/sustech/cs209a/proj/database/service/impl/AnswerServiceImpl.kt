package io.github.octcarp.sustech.cs209a.proj.database.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import io.github.octcarp.sustech.cs209a.proj.database.entity.AnswerPO
import io.github.octcarp.sustech.cs209a.proj.database.mapper.AnswerMapper
import io.github.octcarp.sustech.cs209a.proj.database.service.AnswerService
import org.springframework.stereotype.Service

@Service
class AnswerServiceImpl : ServiceImpl<AnswerMapper, AnswerPO>(), AnswerService {
}