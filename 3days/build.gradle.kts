plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.plugin) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false

    id("org.openapi.generator") version "7.8.0" apply true
}

buildscript {
    repositories {
        maven(url = "https://repo1.maven.org/maven2")
    }
}

val targetDir = "${rootDir.path.replace("\\", "/")}/core/openapi"

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("generateOpenAPI") {
    dependsOn("downloadOpenApiSpec")

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
    inputSpec.set("$targetDir/source/openapi.yaml")
    outputDir.set("$rootDir/core/network")

    packageName.set("com.weave.network")
    apiPackage.set("com.weave.network.api")
    modelPackage.set("com.weave.network.model")
    templateDir.set("$targetDir/template")
    ignoreFileOverride.set("${rootDir.path.replace("\\", "/")}/core/network/.openapi-generator-ignore")

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

tasks.register<Sync>("downloadOpenApiSpec") {
    val repoUrl = "https://github.com/Student-Center/3days-oas.git"
    val outputDir = file("${rootDir.path.replace("\\", "/")}/core/openapi/source")

    println()
    println("========".repeat(10))
    println("[Task Start] Downloading OpenAPI Specification YAML Files...")
    println("OutputDir path: ${outputDir.path}")
    println("========".repeat(10))

    if (outputDir.exists()) {
        println("OpenAPI Spec files already exist. Removing existing files...")
        outputDir.deleteRecursively()
    }

    exec {
        println("[$repoUrl] Cloning repository...")
        commandLine("git", "clone", repoUrl, outputDir.absolutePath)
        standardOutput = System.out
        errorOutput = System.err
        isIgnoreExitValue = true
    }.let { execResult ->
        println("========".repeat(10))
        if (execResult.exitValue != 0) {
            println("[Task End] Cloning failed with exit code: ${execResult.exitValue}")
        } else {
            println("[Task End] Completed successfully: OpenAPI Spec files have been created.")
        }
        println("========".repeat(10))
    }
}