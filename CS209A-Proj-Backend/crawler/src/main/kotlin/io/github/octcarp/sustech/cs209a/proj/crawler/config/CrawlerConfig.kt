package io.github.octcarp.sustech.cs209a.proj.crawler.config

import java.util.Properties

object CrawlerConfig {
    private const val CONFIG_FILE_PATH = "/config.properties"

    val BASE_URL: String
    val SITE: String
    val TAG: String
    val API_KEY: String
    val FILTER: String

    val JSON_DIR: String

    init {
        readPropertiesFile().let {
            BASE_URL = it.getProperty("base_url", "https://api.stackexchange.com/2.3")
            TAG = it.getProperty("tag", "java")
            SITE = it.getProperty("site", "stackoverflow")
            API_KEY = it.getProperty("api_key", "rl_QSEp4LTsXZLcivf3gjbjQJAe3")
            FILTER = it.getProperty("filter", "!*fH2K9ewS1UcdW(pqYr*DX.X(GHj0XUwszGHe")

            JSON_DIR = it.getProperty("json_dir", "/data/final")
        }
    }

    private fun readPropertiesFile(): Properties {
        val properties = Properties()
        CrawlerConfig::class.java.getResourceAsStream(CONFIG_FILE_PATH).use { inputStream ->
            properties.load(inputStream)
        }
        return properties
    }
}