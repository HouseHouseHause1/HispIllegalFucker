plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenLocal()
    maven { url = uri("https://repo.txmc.me/releases") }
    maven { url = uri("https://repo.maven.apache.org/maven2/") }
}

dependencies {
    compileOnly("com.destroystokyo.paper:paper-jar:1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.24")
    compileOnly(files(File("${project.projectDir}/lib/PaperAPIExtensions-1.0-SNAPSHOT-all.jar").absolutePath))
    annotationProcessor("org.projectlombok:lombok:1.18.22")
}

tasks.shadowJar {
    exclude("pom.*")
    minimize()
}

group = "me.sevj6"
version = "1.0-SNAPSHOT"
description = "IllegalFucker"
java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
