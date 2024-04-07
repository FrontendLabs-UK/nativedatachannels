import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.`maven-publish`

plugins {
    `maven-publish`
    signing
}

publishing {
    // Configure all publications
    publications.withType<MavenPublication> {
        // Stub javadoc.jar artifact
        artifact(tasks.register("${name}JavadocJar", Jar::class) {
            archiveClassifier.set("javadoc")
            archiveAppendix.set(this@withType.name)
        })

        // Provide artifacts information required by Maven Central
        pom {
            name.set("LibNativeDatachannels")
            description.set("Native DataChannels library for WebRTC")
            url.set("https://github.com/FrontendLabs-UK/nativedatachannels")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            developers {
                developer {
                    id.set("Frontendlabs")
                    name.set("Frontendlabs-UK")
                    organization.set("Frontendlabs")
                    organizationUrl.set("https://github.com/FrontendLabs-UK")
                }
            }
            scm {
                url.set("https://github.com/FrontendLabs-UK/nativedatachannels")
            }
        }
    }
}

signing {
    if (project.hasProperty("signing.gnupg.keyName")) {
        useGpgCmd()
        sign(publishing.publications)
    }
}
