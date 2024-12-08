plugins {
    java
    kotlin("jvm") version "2.1.0" apply false
}

group = "io.github.octcarp.sustech.cs209a.proj"
version = "1.0-SNAPSHOT"

ext {
    set("springBootVersion", "3.3.4")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}