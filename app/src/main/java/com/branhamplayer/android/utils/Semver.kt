package com.branhamplayer.android.utils

class Semver(private val version: String) {

    private val major: Int
    private val minor: Int
    private val patch: Int

    init {
        val parts = version.split(".")

        if (parts.size != 3) {
            throw IllegalArgumentException("$version does not conform to semver conventions")
        }

        major = parts[0].toInt()
        minor = parts[1].toInt()
        patch = parts[2].toInt()
    }

    operator fun compareTo(semver: Semver): Int {
        val theseParts = arrayOf(major, minor, patch)
        val thoseParts = arrayOf(semver.major, semver.minor, semver.patch)

        for (i in 0..2) {
            if (theseParts[i] > thoseParts[i]) {
                return 1
            } else if (theseParts[i] < thoseParts[i]) {
                return -1
            }
        }

        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Semver) return false

        val theseParts = arrayOf(major, minor, patch)
        val thoseParts = arrayOf(other.major, other.minor, other.patch)

        for (i in 0..2) {
            if (theseParts[i] != thoseParts[i]) {
                return false
            }
        }

        return true
    }

    override fun hashCode() = toString().hashCode()
    override fun toString() = "$major.$minor.$patch"
}
