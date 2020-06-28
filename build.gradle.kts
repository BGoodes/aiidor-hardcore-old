plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("java")
}

group = "fr.aiidor"
version = "2.0.0"

val targetJavaVersion = JavaVersion.VERSION_1_8
java {
    sourceCompatibility = targetJavaVersion
    targetCompatibility = targetJavaVersion
    if (JavaVersion.current() < targetJavaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion.majorVersion))
    }
}

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    mavenCentral()
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.1-R0.1-SNAPSHOT")
    compileOnly("com.googlecode.json-simple:json-simple:1.1.1")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}