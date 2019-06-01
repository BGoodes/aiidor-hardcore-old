plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("java")
}

group = "fr.aiidor"
version = "1.5.0"

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
    compileOnly("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}