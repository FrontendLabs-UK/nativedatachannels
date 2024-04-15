import gradle.kotlin.dsl.accessors._160bede918d6d3d66424e86a2fb07cac.nexusPublishing
import gradle.kotlin.dsl.accessors._2502cef48cff830615fe1c6d6ab5e104.publishing
import java.util.Properties

plugins {
    id("io.github.gradle-nexus.publish-plugin")
    `maven-publish`
}

allprojects {
    group = "uk.frontendlabs.libdatachannel"
    version = "0.0.10-SNAPSHOT"
}

//nexusPublishing {
//    // Configure maven central repository
//    // https://github.com/gradle-nexus/publish-plugin#publishing-to-maven-central-via-sonatype-ossrh
//    repositories {
//        sonatype {  //only for users registered in Sonatype after 24 Feb 2021
//            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
//            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
//        }
//    }
//}
