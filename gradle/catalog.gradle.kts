dependencyResolutionManagement {
    versionCatalogs {
        create("app") {
            version("compileSdk", "31")
            version("minSdk", "25")
        }

        create("libs") {
            version("kotlin", "1.6.10")

            alias("kotlin-gradle-plugin").to("org.jetbrains.kotlin", "kotlin-gradle-plugin").versionRef("kotlin")
            alias("kotlin-stdlib").to("org.jetbrains.kotlin", "kotlin-stdlib").versionRef("kotlin")


            alias("android-gradle-plugin").to("com.android.tools.build", "gradle").version("7.0.4")
        }
    }
}
