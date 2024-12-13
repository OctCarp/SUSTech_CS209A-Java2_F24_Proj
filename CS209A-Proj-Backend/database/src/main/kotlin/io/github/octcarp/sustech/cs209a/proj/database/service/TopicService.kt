package io.github.octcarp.sustech.cs209a.proj.database.service

import com.baomidou.mybatisplus.extension.service.IService
import io.github.octcarp.sustech.cs209a.proj.database.entity.TopicPO

interface TopicService: IService<TopicPO> {
    fun getTopicByName(name: String): TopicPO?
}