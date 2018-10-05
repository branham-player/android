package tech.oliver.branhamplayer.android.services.logging

import tech.oliver.branhamplayer.android.BuildConfig

object LogglyConstants {

    const val environment = BuildConfig.LOGGLY_ENVIRONMENT

    object LogTypes {
        const val verbose = "verbose"
        const val debug = "debug"
        const val info = "info"
        const val warn = "warn"
        const val error = "error"
    }

    enum class Tags(val tagName: String) {
        NOTIFICATION("notification"),
        PERMISSIONS("permissions"),
        PLAYER("player"),
        PLAYER_VIEW_MODEL("player-view-model"),
        REPOSITORY("repository"),
        SERMON_LIBRARY("sermon-library")
    }
}
