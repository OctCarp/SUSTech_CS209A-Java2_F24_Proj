plugins {
    java
    kotlin("jvm") version "2.1.0" apply false
    kotlin("plugin.spring") version "2.1.0" apply false
    kotlin("plugin.serialization") version "2.1.0" apply false
    id("io.freefair.lombok") version "8.11" apply false

    id("org.springframework.boot") version "3.3.4" apply false
    id("io.spring.dependency-management") version "1.1.6" apply false
}

group = "io.github.octcarp.sustech.cs209a.proj"
version = "1.0-SNAPSHOT"

ext {
    set("springBootVersion", "3.3.4")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.0")
}