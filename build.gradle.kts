import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import dev.s7a.gradle.minecraft.server.tasks.LaunchMinecraftServerTask
import dev.s7a.gradle.minecraft.server.tasks.LaunchMinecraftServerTask.JarUrl
import groovy.lang.Closure
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    kotlin("jvm") version "1.6.10"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("com.github.ben-manes.versions") version "0.41.0"
    id("com.palantir.git-version") version "0.12.3"
    id("dev.s7a.gradle.minecraft.server") version "1.2.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.jmailen.kotlinter") version "3.8.0"
}

val gitVersion: Closure<String> by extra

val pluginVersion: String by project.ext

repositories {
    mavenCentral()
    maven(url = "https://repo.papermc.io/repository/maven-public")
    maven(url = "https://oss.sonatype.org/content/groups/public/")
    maven(url = "https://repo.codemc.org/repository/maven-public/")
}

val shadowImplementation: Configuration by configurations.creating
configurations["implementation"].extendsFrom(shadowImplementation)

dependencies {
    shadowImplementation(kotlin("stdlib"))
    compileOnly "io.papermc.paper:paper-api:$pluginVersion-R0.1-SNAPSHOT"
    compileOnly "dev.jorel:commandapi-core:8.5.1"
}

configure<BukkitPluginDescription> {
    main = "@group@.Main"
    version = gitVersion()
    apiVersion = "1." + pluginVersion.split(".")[1]
}

kotlinter {
    ignoreFailures = true
}

tasks.withType<ShadowJar> {
    configurations = listOf(shadowImplementation)
    archiveClassifier.set("")
    relocate("kotlin", "@group@.libs.kotlin")
    relocate("org.intellij.lang.annotations", "@group@.libs.org.intellij.lang.annotations")
    relocate("org.jetbrains.annotations", "@group@.libs.org.jetbrains.annotations")
}

tasks.named("build") {
    dependsOn("shadowJar")
}

listOf(
    "1.13.2",
    "1.14.4",
    "1.15.2",
    "1.16.5",
    "1.17.1",
    "1.18.2",
    "1.19.2"
).forEach { version ->
    task<LaunchMinecraftServerTask>("buildAndLaunchServer-$version") {
        dependsOn("build")
        doFirst {
            copy {
                from(buildDir.resolve("libs/${project.name}.jar"))
                into(buildDir.resolve("MinecraftServer-$version/plugins"))
            }
        }

        jarUrl.set(JarUrl.Paper($version))
        jarName.set("server.jar")
        serverDirectory.set(buildDir.resolve("MinecraftServer-$version"))
        nogui.set(true)
        agreeEula.set(true)
    }
}

task<SetupTask>("setup")
