plugins {
    kotlin("jvm")
}

group = "io.github.octcarp.sustech.cs209a.proj"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(libs.bundles.database)
    runtimeOnly("org.postgresql:postgresql:42.7.4")

    api("com.baomidou:mybatis-plus-spring-boot3-starter:3.5.9")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}