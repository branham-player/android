package com.branhamplayer.android

object StartupConstants {
    object FirebaseRemoteConfig {
        const val cacheExipration = 3600L // In seconds
        const val logLevel = "log_level"
    }

    object Intents {
        const val googlePlay = "https://play.google.com/store/apps/details?id=com.branhamplayer.android"
        const val sermonsModule = "https://branhamplayer.com/sermons"
    }

    object PreflightChecklist {
        const val message = "message"
        const val minimumVersion = "minimum_version"
        const val platformStatus = "platform_available"
    }
}
