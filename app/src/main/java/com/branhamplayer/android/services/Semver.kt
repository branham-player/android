package com.branhamplayer.android.services

class Semver(version: String) {

    private var major: Int
    private var minor: Int
    private var patch: Int

    init {
        val parts = version.split(".")

        if (parts.size != 3) {
            throw IllegalArgumentException("$version does not conform to semver conventions")
        }

        major = parts[0].toInt()
        minor = parts[1].toInt()
        patch = parts[2].toInt()
    }

    operator fun compareTo(semver: Semver) = when {
        major > semver.major && minor > semver.minor && patch > semver.patch -> 1
        major < semver.major && minor < semver.minor && patch < semver.patch -> -1
        else -> 0
    }
}
