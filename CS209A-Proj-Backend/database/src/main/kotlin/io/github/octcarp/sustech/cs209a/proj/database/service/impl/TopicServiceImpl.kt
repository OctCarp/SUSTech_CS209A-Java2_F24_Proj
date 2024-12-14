package io.github.octcarp.sustech.cs209a.proj.database.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import io.github.octcarp.sustech.cs209a.proj.database.entity.TopicPO
import io.github.octcarp.sustech.cs209a.proj.database.mapper.TopicMapper
import io.github.octcarp.sustech.cs209a.proj.database.service.TopicService
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Retry.Topic
import org.springframework.stereotype.Service
import javax.management.Query.eq

@Service
class TopicServiceImpl : ServiceImpl<TopicMapper, TopicPO>(), TopicService {
    override fun getTopicByName(name: String): TopicPO? {
        val queryWrapper = KtQueryWrapper(TopicPO::class.java).eq(TopicPO::topicName, name)
        return baseMapper.selectOne(queryWrapper)
    }
}