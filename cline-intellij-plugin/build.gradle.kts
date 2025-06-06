import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType

plugins {
    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
    // Gradle IntelliJ Plugin
    id("org.jetbrains.intellij.platform") version "2.0.0-beta2"
    // Gradle Changelog Plugin
    id("org.jetbrains.changelog") version "2.2.0"
    // Gradle Qodana Plugin
    id("org.jetbrains.qodana") version "0.1.13"
}

group = "com.example.cline"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Configure IntelliJ Platform settings.
intellijPlatform {
    // Properties of the IntelliJ Platform that will be used to build the plugin.
    // Consult https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-extension.html
    // for more details.

    // The version of the IntelliJ Platform to build against.
    // TODO: Specify the IDE version the plugin is targeting.
    //       See: https://plugins.jetbrains.com/docs/intellij/build-number-ranges.html
    pluginConfiguration {
        version = "2023.3" // Example: Use a recent stable version
        type = IntelliJPlatformType.IC // Target IntelliJ IDEA Community
    }


    // Sandbox an IDE instance for running and debugging the plugin.
    // TODO: Specify the IDE version to use for the sandbox.
    //       See: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-tasks.html#runIdeTask
    sandboxContainer {
        type = IntelliJPlatformType.IC
        version = "2023.3" // Example: Use a version compatible with pluginConfiguration
    }
}


// Configure the changelog plugin - see https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    groups.empty() // Use default grouping
    // Provide the path to the changelog file:
    // unreleasedTerm.set("[Unreleased]") // Customize the unreleased version marker if needed
    // groups.set(emptyList()) // Disable grouping by commit types if not using conventional commits

    // Optional: Add a header to the changelog HTML output
    // header.set(provider { project.name })

    // Optional: Specify the changelog file name if it's not CHANGELOG.md
    // path.set(file("CHANGES.md").absolutePath)
}


// Configure the Qodana plugin - see https://github.com/JetBrains/gradle-qodana-plugin
qodana {
    // Customize Qodana analysis options if needed
    // Example:
    // projectPath.set(project.projectDir.absolutePath)
    // resultsPath.set(file("build/qodana").absolutePath)
    // reportPath.set(file("build/qodana/report").absolutePath)
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        // Extract the <!-- ... --> description block from the README.md file.
        // TODO: Create a README.md file with a description of your plugin.
        //       The description of your plugin will be extracted from the H2 section.
        //       See https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-tasks.html#patchpluginxmltask
        // pluginDescription.set(
        //    provider {
        //        val file = project.file("README.md")
        //        if (!file.exists()) {
        //            return@provider ""
        //        }
        //        val lines = file.readLines()
        //        val h2Index = lines.indexOfFirst { it.startsWith("## Description") }
        //        if (h2Index == -1) {
        //            return@provider ""
        //        }
        //        lines.subList(h2Index + 1, lines.size)
        //            .takeWhile { !it.startsWith("##") }
        //            .joinToString("\n")
        //            .trim()
        //    }
        // )

        // Get the latest available change notes from the changelog file.
        changeNotes.set(provider { changelog.getLatest().toHTML() })
    }

    // Configure UI tests plugin
    // Read more: https://github.com/JetBrains/intellij-ui-test-robot
    // runIdeForUiTests {
    //    systemProperty("robot-server.port", "8082")
    //    systemProperty("ide.mac.message.dialogs.as.sheets", "false")
    //    systemProperty("jb.privacy.policy.text", "<!--999.999-->")
    //    systemProperty("jb.consents.confirmation.enabled", "false")
    // }

    signPlugin {
        // Configure signing options if needed
        // See: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-tasks.html#signplugintask
        // certificateChainFile.set(file("cert/chain.crt"))
        // privateKeyFile.set(file("cert/private.key"))
        // password.set(System.getenv("SIGNING_PASSWORD")) // Or use a properties file
    }

    publishPlugin {
        // Configure publishing options if needed
        // See: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-tasks.html#publishplugintask
        // token.set(System.getenv("PUBLISH_TOKEN")) // Or use a properties file
        // channels.set(listOf("default", "eap")) // Example: Publish to default and EAP channels
    }
}
