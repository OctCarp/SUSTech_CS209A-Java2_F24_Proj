package io.github.octcarp.sustech.cs209a.proj.database.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import io.github.octcarp.sustech.cs209a.proj.database.entity.PostTopicPO
import io.github.octcarp.sustech.cs209a.proj.database.mapper.PostTopicMapper
import io.github.octcarp.sustech.cs209a.proj.database.service.PostTopicService
import org.springframework.stereotype.Service

@Service
class PostTopicServiceImpl : ServiceImpl<PostTopicMapper, PostTopicPO>(), PostTopicService {
}