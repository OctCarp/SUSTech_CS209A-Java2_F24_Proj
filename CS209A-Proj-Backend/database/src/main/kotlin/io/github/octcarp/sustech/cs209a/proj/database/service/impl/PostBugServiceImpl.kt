package io.github.octcarp.sustech.cs209a.proj.database.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import io.github.octcarp.sustech.cs209a.proj.database.entity.PostBugPO
import io.github.octcarp.sustech.cs209a.proj.database.mapper.PostBugMapper
import io.github.octcarp.sustech.cs209a.proj.database.service.PostBugService
import org.springframework.stereotype.Service

@Service
class PostBugServiceImpl : ServiceImpl<PostBugMapper, PostBugPO>(), PostBugService {
}