package io.github.octcarp.sustech.cs209a.proj.database.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import io.github.octcarp.sustech.cs209a.proj.database.entity.CommentPO
import io.github.octcarp.sustech.cs209a.proj.database.mapper.CommentMapper
import io.github.octcarp.sustech.cs209a.proj.database.service.CommentService
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl : ServiceImpl<CommentMapper, CommentPO>(), CommentService {
}