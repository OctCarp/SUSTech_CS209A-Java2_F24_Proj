package io.github.octcarp.sustech.cs209a.proj.database.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import io.github.octcarp.sustech.cs209a.proj.database.entity.TopicPO
import io.github.octcarp.sustech.cs209a.proj.database.mapper.TopicMapper
import io.github.octcarp.sustech.cs209a.proj.database.service.TopicService
import org.springframework.stereotype.Service

@Service
class TopicServiceImpl : ServiceImpl<TopicMapper, TopicPO>(), TopicService {
}