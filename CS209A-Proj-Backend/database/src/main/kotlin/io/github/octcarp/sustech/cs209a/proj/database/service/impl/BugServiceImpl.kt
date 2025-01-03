package io.github.octcarp.sustech.cs209a.proj.database.service.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import io.github.octcarp.sustech.cs209a.proj.database.entity.BugPO
import io.github.octcarp.sustech.cs209a.proj.database.mapper.BugMapper
import io.github.octcarp.sustech.cs209a.proj.database.service.BugService
import org.springframework.stereotype.Service

@Service
class BugServiceImpl : ServiceImpl<BugMapper, BugPO>(), BugService {
    override fun getBugByName(name: String): BugPO? {
        val queryWrapper = KtQueryWrapper(BugPO::class.java).eq(BugPO::bugName, name)
        return baseMapper.selectOne(queryWrapper)
    }
}