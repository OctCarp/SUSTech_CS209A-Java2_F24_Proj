plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.serialization")
}

group = "io.github.octcarp.sustech.cs209a.proj"
version = "1.0-SNAPSHOT"

val nlpVersion = "4.5.7"

dependencies {
    implementation(project(":common"))
    implementation(project(":database"))

    implementation(libs.bundles.crawler)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("com.google.guava:guava:33.3.1-jre")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("edu.stanford.nlp:stanford-corenlp:$nlpVersion")
    implementation("edu.stanford.nlp:stanford-corenlp:$nlpVersion:models")
    implementation("edu.stanford.nlp:stanford-corenlp:$nlpVersion:models-english")
    implementation("edu.stanford.nlp:stanford-parser:3.9.2")

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