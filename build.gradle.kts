import org.bxteam.runserver.ServerType

plugins {
    java
    id("com.gradleup.shadow") version "9.4.1"
    id("org.bxteam.runserver") version "1.2.2"
}

group = "us.polarismc"
version = "1.0.0-dev"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.codemc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")

    implementation("fr.mrmicky:fastboard:2.1.5")
    implementation("org.reflections:reflections:0.10.2")
    implementation("fr.mrmicky:FastInv:v3.1.2")
    implementation("com.zaxxer:HikariCP:7.0.2")
    implementation("com.github.putindeer:mcdev-utils:1.0.28")
    implementation("de.rapha149.signgui:signgui:2.5.4")
    implementation("net.kyori:adventure-api:5.0.1")
    implementation("net.kyori:adventure-text-minimessage:5.0.1")

    compileOnly("org.projectlombok:lombok:1.18.44")
    annotationProcessor("org.projectlombok:lombok:1.18.44")

    compileOnly("me.clip:placeholderapi:2.12.2")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.3.0")
    compileOnly("org.popcraft:chunky-common:1.4.57")

    testImplementation("org.mockbukkit.mockbukkit:mockbukkit-v1.21:4.110.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.44")
}

sourceSets {
    main {
        resources {
            srcDir("src/main/resources")
        }
    }
    test {
        java {
            srcDir("src/test/java")
        }
    }
}

tasks.runServer {
    serverType(ServerType.PAPER)
    serverVersion("1.21.11")
    noGui(true)
    acceptMojangEula()
    downloadPlugins {
        jenkins("https://ci.athion.net", "FastAsyncWorldEdit", Regex("Bukkit"))
        url("https://download.luckperms.net/1581/bukkit/loader/LuckPerms-Bukkit-5.4.164.jar")
    }
    inputTask(tasks.named("shadowJar"))
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    filesMatching(listOf("plugin.yml", "paper-plugin.yml", "config.yml", "*.yml", "*.yaml", "*.json", "*.properties")) {
        expand(project.properties)
    }
}

tasks.shadowJar {
    archiveClassifier.set("")
    relocate("de.rapha149.signgui", "us.polarismc.polarisuhc.signgui")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.test {
    useJUnitPlatform()
}
