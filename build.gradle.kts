plugins {
    id("build-standard-jetbrains-plugin-build")
}

// As of 2025-09-21, this fails with IU; disabling.
intellijPlatform.buildSearchableOptions = false