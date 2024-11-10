rootProject.name = "CS209A-Proj-Backend"

include(
    "common",
    "crawler",
    "service"
)


// Configure the repositories for all modules
dependencyResolutionManagement {
    repositories {
        // Aliyun Maven Repository
        maven("https://maven.aliyun.com/repository/public")
        // Aliyun Gradle Plugin Repository
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        // Maven Central repository
        mavenCentral()
        // Gradle plugins repository
        maven("https://plugins.gradle.org/m2/")
    }
}

