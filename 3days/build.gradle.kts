plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.plugin) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false

    id("com.google.gms.google-services") version "4.4.2" apply false
    id("org.openapi.generator") version "7.8.0" apply true
}

buildscript {
    repositories {
        maven(url = "https://repo1.maven.org/maven2")
    }
}

val targetDir = "${rootDir.path.replace("\\", "/")}/core/3days-oas"

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("generateOpenAPI") {
    dependsOn("updateSubModule")

    doFirst {
        val outputDir = File("$rootDir/core/network")
        if (outputDir.exists()) {
            println("========".repeat(10))
            println("OpenAPI Spec files already generated. Removing previous files...")
            outputDir.deleteRecursively()
            println("========".repeat(10))
            println()
        }
    }

    generatorName.set("kotlin")
    inputSpec.set("$targetDir/openapi.yaml")
    outputDir.set("$rootDir/core/network")

    packageName.set("com.weave.network")
    apiPackage.set("com.weave.network.api")
    modelPackage.set("com.weave.network.model")
    templateDir.set("$rootDir/core/utils/src/main/java/com/weave/utils/oas")

    generateApiTests.set(false)
    generateModelTests.set(false)
    generateApiDocumentation.set(false)
    generateModelDocumentation.set(false)

    configOptions.set(
        mapOf(
            "library" to "jvm-retrofit2",
            "serializationLibrary" to "gson",
            "useCoroutines" to "true",
            "omitGradleWrapper" to "true",
            "enumPropertyNaming" to "UPPERCASE",
            "useTags" to "true",
        )
    )
}

tasks.register<Sync>("updateSubModule") {

    exec {
        println("========".repeat(10))
        println("[Task Start] Update Submodule...")
        commandLine("git", "submodule", "update", "--remote")
    }.let { execResult ->
        if (execResult.exitValue != 0) {
            println("[Task End] Update failed with exit code: ${execResult.exitValue}")
        } else {
            println("[Task End] Completed successfully: OpenAPI Spec files have been updated.")
        }
        println("========".repeat(10))
    }
}