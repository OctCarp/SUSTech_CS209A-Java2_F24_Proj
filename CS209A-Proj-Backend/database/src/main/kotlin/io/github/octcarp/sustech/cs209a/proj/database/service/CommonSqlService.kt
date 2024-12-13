package io.github.octcarp.sustech.cs209a.proj.database.service

import io.github.octcarp.sustech.cs209a.proj.database.mapper.CommonSqlMapper
import org.springframework.stereotype.Service

@Service
class CommonSqlService(
    private val commonSqlMapper: CommonSqlMapper
) {
    fun testConnection() = commonSqlMapper.testConnection()

    fun disableAllTriggers() = commonSqlMapper.disableAllTriggers()

    fun enableAllTriggers() = commonSqlMapper.enableAllTriggers()
}