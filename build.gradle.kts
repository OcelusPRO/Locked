import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.1.10"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "fr.ftnl.locked"
version = "1.1"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    
}

dependencies {
    testImplementation(kotlin("test"))
    
    compileOnly("org.slf4j:slf4j-api:2.0.7")
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    implementation("com.google.code.gson:gson:2.12.1")
    implementation("com.github.twitch4j:twitch4j:1.24.0")
    implementation("com.github.philippheuer.events4j:events4j-handler-reactor:0.12.2")
    
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(18)
}


tasks.shadowJar {
    manifest {
        attributes["paperweight-mappings-namespace"] = "mojang"
    }
}