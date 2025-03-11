
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
    implementation("com.github.twitch4j:twitch4j:1.24.0")
    
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

tasks.register("copyWithoutBuildNumber") {
    doLast {
        copy {
            from(tasks.shadowJar.get().archiveFile)
            into(layout.buildDirectory.dir("libs"))
            rename {fileName ->
                fileName.replace("-${buildNumber}", "")
            }
        }
        copy {
            from(tasks.shadowJar.get().archiveFile)
            into(layout.buildDirectory.dir("libs"))
            rename {fileName -> fileName.replace("${project.version}-${buildNumber}", "")}
        }
    }
}


tasks.shadowJar {
    dependsOn("incrementBuildNumber")
    
    manifest {
        attributes["paperweight-mappings-namespace"] = "mojang"
    }
    
    relocate("com.fasterxml.jackson", "shaded.com.fasterxml.jackson")
    
    archiveBaseName.set("Locked") // Nom de votre JAR
    archiveClassifier.set("") // Supprime le suffixe "-all"
    archiveVersion.set("${project.version}-${buildNumber}")
    destinationDirectory.set(file(versionedDir))
    
    mergeServiceFiles()
    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
    exclude("META-INF/*.RSA")
    
    finalizedBy("copyWithoutBuildNumber")
}



tasks.build {
    dependsOn(tasks.shadowJar)
}