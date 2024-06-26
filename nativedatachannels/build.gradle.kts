import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("module.publication")
    `maven-publish`
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    targetHierarchy.default()
    jvm {
        mainRun {
            mainClass.set("MainKt")
        }
    }
    androidTarget {
        publishLibraryVariants("debug", "release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

//    val commonNativeConfig: KotlinNativeCompilation.() -> Unit = {
//        cinterops {
//            val datachannel by creating {
//                defFile(project.file("cinterop/libdatachannel/libdatachannel.def"))
//                packageName("com.libdatachannel")
//                compilerOpts("-I${project.file("cinterop/libdatachannel").absolutePath}")
//                includeDirs.allHeaders(project.file("cinterop/libdatachannel").absolutePath)
//            }
//        }
//    }
//    listOf(
//        "armeabi-v7a" to androidNativeArm32(),
//        "arm64-v8a" to androidNativeArm64(),
//        "x86_64" to androidNativeX64(),
//        "x86" to androidNativeX86(),
//    ).forEach { (targetTriple, nativeTarget) ->
//        nativeTarget.compilations.all {
//            commonNativeConfig()
//        }

//        nativeTarget.binaries.sharedLib {
//            baseName = "datachannel_native"
//            outputDirectory = project.file("artifacts/android-native/$targetTriple")
//            this.linkerOpts(
//                "-Wl,--whole-archive",
//                project.file("libs/android/$targetTriple/libcrypto.a").absolutePath,
//                project.file("libs/android/$targetTriple/libssl.a").absolutePath,
//                project.file("libs/android/$targetTriple/libdatachannel.so").absolutePath,
//                "-Wl,--no-whole-archive",
//                "-L${project.file("libs/android/$targetTriple").absolutePath}",
//                "-lc++_shared"
//            )
//        }
//    }
    
//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64(),
//        linuxX64(),
//        macosArm64()
//    ).forEach { nativeTarget ->
//        nativeTarget.compilations.all {
//            commonNativeConfig()
//        }
//
//        nativeTarget.binaries.staticLib {
//            baseName = "datachannel_native"
//            this.outputDirectory = project.file("artifacts/${nativeTarget.targetName}")
//        }
//    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                //put your multiplatform dependencies here
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val commonJvm by creating {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.json)
                api(libs.jna.platform)
            }
        }
        
        val androidMain by getting {
            dependencies {
                dependsOn(commonJvm)
            }
        }

        val jvmMain by getting {
            dependencies {
                dependsOn(commonJvm)
            }
        }
    }
}

android {
    namespace = "uk.frontendlabs.nativedatachannel"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    
    sourceSets.all {
        jniLibs.srcDirs("artifacts/android-native")
    }
}

val ext: MutableMap<String, Any> = mutableMapOf()
fun getExtraString(name: String) = ext[name]?.toString()

val secretPropsFile: File = project.rootProject.file("local.properties")
if (secretPropsFile.exists()) {
    secretPropsFile.reader().use {
        Properties().apply {
            load(it)
        }
    }.onEach { (name, value) ->
        ext[name.toString()] = value
    }
} else {
    ext["gpr.user"] = System.getenv("GPR_USER")
    ext["gpr.key"] = System.getenv("GPR_KEY")
}

publishing {
    repositories.maven {
        name = "Repsy"
        url = uri("https://repo.repsy.io/mvn/frontendlabs/nativedatachannels")
        credentials {
            username = getExtraString("gpr.user")
            password = getExtraString("gpr.key")
        }
    }
}
