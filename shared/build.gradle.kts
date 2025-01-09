import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Locale

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.skie)
    alias(libs.plugins.sqldelight)
}

android {
    namespace = "co.touchlab.spotnotes"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

kotlin {
    val shouldTargetIOs = System.getProperty("os.name").lowercase(Locale.US).startsWith("mac")
    androidTarget()
    if (shouldTargetIOs) {
        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = "Shared"
                /**
                 * https://github.com/sqldelight/sqldelight/issues/1442#issuecomment-615991279
                 * Since I cannot use XCode to add a linker flag there, we have to make the
                 * framework dynamic, which would otherwise be a questionable choice with our setup.
                 * It is what it is
                 */
                linkerOpts.add("-lsqlite3")
            }
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.activity.ktx)
            implementation(libs.core.ktx)
            implementation(libs.sqldelight.driver.android)
        }
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines)
            implementation(libs.kotlinx.datetime)
            implementation(libs.bundles.compass)
            implementation(libs.sqldelight.extension.coroutines)
        }
        if (shouldTargetIOs) {
            iosMain.dependencies {
                implementation(libs.sqldelight.driver.native)
            }
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sqldelight {
        databases {
            create("Database") {
                packageName = "co.touchlab.spotnotes.db"
            }
        }
    }
}
