package io.github.octcarp.sustech.cs209a.proj.database.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import io.github.octcarp.sustech.cs209a.proj.database.entity.UserPO
import io.github.octcarp.sustech.cs209a.proj.database.mapper.UserMapper
import io.github.octcarp.sustech.cs209a.proj.database.service.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : ServiceImpl<UserMapper, UserPO>(), UserService {
}