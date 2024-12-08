package io.github.octcarp.sustech.cs209a.proj.database.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import io.github.octcarp.sustech.cs209a.proj.database.entity.QuestionPO
import io.github.octcarp.sustech.cs209a.proj.database.mapper.QuestionMapper
import io.github.octcarp.sustech.cs209a.proj.database.service.QuestionService
import org.springframework.stereotype.Service

@Service
class QuestionServiceImpl : ServiceImpl<QuestionMapper, QuestionPO>(), QuestionService {
}