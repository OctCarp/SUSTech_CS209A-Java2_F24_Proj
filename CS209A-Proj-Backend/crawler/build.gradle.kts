plugins {
    kotlin("jvm")
}

group = "io.github.octcarp.sustech.cs209a.proj"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":common"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}