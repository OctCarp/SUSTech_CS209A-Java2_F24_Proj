package io.github.octcarp.sustech.cs209a.proj.database.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import io.github.octcarp.sustech.cs209a.proj.database.entity.TagPO
import io.github.octcarp.sustech.cs209a.proj.database.mapper.TagMapper
import io.github.octcarp.sustech.cs209a.proj.database.service.TagService
import org.springframework.stereotype.Service

@Service
class TagServiceImpl : ServiceImpl<TagMapper, TagPO>(), TagService {
}