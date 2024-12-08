plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "2.0.20"
}

group = "io.github.octcarp.sustech.cs209a.proj"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":common"))
    implementation(project(":database"))

    implementation(libs.bundles.crawler)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("com.google.guava:guava:33.3.1-jre")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("edu.stanford.nlp:stanford-corenlp:4.5.7")

    testImplementation(kotlin("test"))
}

tasks.register<JavaExec>("runFetcher") {
    mainClass.set("io.github.octcarp.sustech.cs209a.proj.crawler.app.FetcherMainKt")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("runImporter") {
    mainClass.set("io.github.octcarp.sustech.cs209a.proj.crawler.app.ImporterMainKt")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.test {
    useJUnitPlatform()
}