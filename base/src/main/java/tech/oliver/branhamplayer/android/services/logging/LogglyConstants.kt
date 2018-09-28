package tech.oliver.branhamplayer.android.services.logging

import tech.oliver.branhamplayer.android.BuildConfig

object LogglyConstants {

    const val environment = BuildConfig.LOGGLY_ENVIRONMENT

    enum class Tags(val tagName: String) {
        NOTIFICATION("notification"),
        PLAYER("player"),
        PLAYER_VIEW_MODEL("player-view-model"),
        REPOSITORY("repository"),
        SERMON_LIBRARY("sermon-library")
    }
}
