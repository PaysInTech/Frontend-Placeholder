import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    val kotlinVersion: String by System.getProperties()
    kotlin("plugin.serialization") version kotlinVersion
    kotlin("js") version kotlinVersion
    val kvisionVersion: String by System.getProperties()
    id("io.kvision") version kvisionVersion
}

version = "1.0.0-SNAPSHOT"
group = "com.paysintech.techsalaries"

repositories {
    mavenCentral()
    jcenter()
    mavenLocal()
}

// Versions
val kotlinVersion: String by System.getProperties()
val kvisionVersion: String by System.getProperties()

val webDir = file("src/main/web")

kotlin {
    js {
        browser {
            runTask {
                outputFileName = "main.bundle.js"
                sourceMaps = false
                devServer = KotlinWebpackConfig.DevServer(
                    open = false,
                    port = 8080,
                    proxy = mutableMapOf(
                        "/kv/*" to "http://localhost:8080",
                        "/kvws/*" to mapOf("target" to "ws://localhost:8080", "ws" to true)
                    ),
                    static = mutableListOf("$buildDir/processedResources/js/main")
                )
            }
            webpackTask {
                outputFileName = "main.bundle.js"
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
        binaries.executable()
    }
    sourceSets["main"].dependencies {
        implementation(npm("sass", "^1.29.0"))
        implementation(npm("sass-loader", "^10.1.0"))
        implementation(npm("marked", "^1.2.4"))
        implementation(npm("smooth-scroll", "^16.1.3"))
        implementation(npm("@types/smooth-scroll", "*"))

        implementation("io.kvision:kvision:$kvisionVersion")
        implementation("io.kvision:kvision-bootstrap:$kvisionVersion")
        implementation("io.kvision:kvision-bootstrap-spinner:$kvisionVersion")
        implementation("io.kvision:kvision-bootstrap-typeahead:$kvisionVersion")
        implementation("io.kvision:kvision-state:$kvisionVersion")
        implementation("io.kvision:kvision-state-flow:$kvisionVersion")
        implementation("io.kvision:kvision-jquery:$kvisionVersion")
    }
    sourceSets["test"].dependencies {
        implementation(kotlin("test-js"))
        implementation("io.kvision:kvision-testutils:$kvisionVersion")
    }
    sourceSets["main"].resources.srcDir(webDir)
}
afterEvaluate {
    tasks {
        create("dist", Copy::class) {
            dependsOn("browserProductionWebpack")
            group = "package"
            destinationDir = file("$buildDir/dist")
            val distribution =
                project.tasks.getByName("browserProductionWebpack", KotlinWebpack::class).destinationDirectory
            from(distribution) {
                include("*.*")
            }
            from(webDir)
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            inputs.files(distribution, webDir)
            outputs.files(destinationDir)
        }
    }
}
