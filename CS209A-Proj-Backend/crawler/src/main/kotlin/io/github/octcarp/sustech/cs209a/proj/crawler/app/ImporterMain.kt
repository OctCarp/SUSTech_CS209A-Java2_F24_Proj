package io.github.octcarp.sustech.cs209a.proj.crawler.app

import io.github.octcarp.sustech.cs209a.proj.crawler.importer.DataAnalysisService
import io.github.octcarp.sustech.cs209a.proj.crawler.importer.DataImportService
import io.github.octcarp.sustech.cs209a.proj.database.service.CommonSqlService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    runApplication<ImporterApplication>()
}

@SpringBootApplication(
    scanBasePackages =
        ["io.github.octcarp.sustech.cs209a.proj.crawler",
            "io.github.octcarp.sustech.cs209a.proj.database"]
)
class ImporterApplication(
    private val commonSqlService: CommonSqlService,
    private val dataImportService: DataImportService,
    private val dataAnalysisService: DataAnalysisService
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        try {
            commonSqlService.disableAllTriggers()

            dataImportService.startImportStaticData()
            dataAnalysisService.startAnalysis()
        } finally {
            commonSqlService.enableAllTriggers()
        }
    }
}
