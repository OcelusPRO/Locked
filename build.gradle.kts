import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.10"
    id("com.gradleup.shadow") version "8.3.6"
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
    
    implementation("com.github.twitch4j:twitch4j:1.24.0") {
        exclude(group = "com.fasterxml.jackson.core")
    }
    
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.18.3"))
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}



// Gestion du numéro de build
val buildNumberFile = file("build.number")
val buildNumber: Int by extra {
    if (buildNumberFile.exists()) buildNumberFile.readText().toInt() else 1
}

tasks.register("incrementBuildNumber") {
    doLast {
        val newNumber = buildNumber + 1
        buildNumberFile.writeText(newNumber.toString())
        println("Build number incrémenté à : $newNumber")
    }
}

val versionedDir = layout.buildDirectory.dir("libs/versioned") // Définir le sous-dossier

// Configuration ShadowJar principale
tasks.shadowJar {
    dependsOn("incrementBuildNumber")
    dependsOn("shadowJarNoVersion")
    
    archiveBaseName.set("Locked")
    archiveClassifier.set("")
    archiveVersion.set("${project.version}-${buildNumber}")
    destinationDirectory.set(versionedDir) // Déplacer le JAR avec numéro de build
    
    manifest {
        attributes["paperweight-mappings-namespace"] = "mojang"
    }
    
    relocate("com.fasterxml.jackson", "shadow.jackson") {
        exclude("com.fasterxml.jackson.module.kotlin.*")
    }
    
    mergeServiceFiles()
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
}

// Tâche pour le JAR sans numéro de build
tasks.register<ShadowJar>("shadowJarNoVersion") {
    dependsOn("incrementBuildNumber")
    
    // Configuration manquante
    mergeServiceFiles()
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    
    // Configuration existante à modifier
    archiveBaseName.set("Locked")
    archiveClassifier.set("")
    archiveVersion.set("")
    destinationDirectory.set(layout.buildDirectory.dir("libs"))
    
    from(sourceSets.main.get().output)
    
    relocate("com.fasterxml.jackson", "shadow.jackson") {
        exclude("com.fasterxml.jackson.module.kotlin.*")
    }
}

tasks.build {
    dependsOn("shadowJar", "shadowJarNoVersion")
}

// Configuration pour éviter les conflits SLF4J
configurations.all {
    exclude(group = "org.slf4j", module = "slf4j-api")
}