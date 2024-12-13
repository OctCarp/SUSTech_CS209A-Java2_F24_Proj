package io.github.octcarp.sustech.cs209a.proj.database

import io.github.octcarp.sustech.cs209a.proj.database.service.CommonSqlService
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    runApplication<DatabaseTestApplication>(*args)
}

@SpringBootApplication(
    scanBasePackages = ["io.github.octcarp.sustech.cs209a.proj.database"]
)
class DatabaseTestApplication(
    val commonSqlService: CommonSqlService
) {
    @PostConstruct
    fun testDatabaseConnection() {
        try {
            val testVal = commonSqlService.testConnection()
            when (testVal) {
                1 -> println("Successfully connected to the database")
                else -> error("Connection has some problem")
            }
        } catch (e: Exception) {
            System.err.println("Failed to connect to the database: ${e.message}")
        }
    }
}
