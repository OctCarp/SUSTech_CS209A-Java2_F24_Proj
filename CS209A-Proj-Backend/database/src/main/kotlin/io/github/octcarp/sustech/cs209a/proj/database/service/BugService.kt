package io.github.octcarp.sustech.cs209a.proj.database.service

import com.baomidou.mybatisplus.extension.service.IService
import io.github.octcarp.sustech.cs209a.proj.database.entity.BugPO

interface BugService: IService<BugPO> {
    fun getBugByName(name: String): BugPO?
}