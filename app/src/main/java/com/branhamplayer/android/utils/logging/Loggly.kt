package com.branhamplayer.android.utils.logging

import com.branhamplayer.android.StartupConstants
import com.orhanobut.logger.Logger
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import timber.log.Timber

class Loggly {

    companion object {
        fun d(tag: LogglyConstants.Tags, message: String, vararg args: Any) {
            if (!allowedToLog(LogglyConstants.LogTypes.debug)) return

            Timber.tag("${tag.tagName}-${LogglyConstants.environment}")
            Timber.d(message, args)
            Logger.d(message, args)
        }

        fun e(tag: LogglyConstants.Tags, message: String, vararg args: Any) {
            if (!allowedToLog(LogglyConstants.LogTypes.error)) return

            Timber.tag("${tag.tagName}-${LogglyConstants.environment}")
            Timber.e(message, args)
            Logger.e(message, args)
        }

        fun e(tag: LogglyConstants.Tags, throwable: Throwable, message: String, vararg args: Any) {
            if (!allowedToLog(LogglyConstants.LogTypes.error)) return

            Timber.tag("${tag.tagName}-${LogglyConstants.environment}")
            Timber.e(throwable, message, args)
            Logger.e(throwable, message, args)
        }

        fun i(tag: LogglyConstants.Tags, message: String, vararg args: Any) {
            if (!allowedToLog(LogglyConstants.LogTypes.info)) return

            Timber.tag("${tag.tagName}-${LogglyConstants.environment}")
            Timber.i(message, args)
            Logger.i(message, args)
        }

        fun v(tag: LogglyConstants.Tags, message: String, vararg args: Any) {
            if (!allowedToLog(LogglyConstants.LogTypes.verbose)) return

            Timber.tag("${tag.tagName}-${LogglyConstants.environment}")
            Timber.v(message, args)
            Logger.v(message, args)
        }

        fun w(tag: LogglyConstants.Tags, message: String, vararg args: Any) {
            if (!allowedToLog(LogglyConstants.LogTypes.warn)) return

            Timber.tag("${tag.tagName}-${LogglyConstants.environment}")
            Timber.w(message, args)
            Logger.w(message, args)
        }

        private fun allowedToLog(currentLogType: String): Boolean {
            val currentLevel = currentLogType.toLogLevel()
            val minLevel = FirebaseRemoteConfig.getInstance().getString(StartupConstants.FirebaseRemoteConfig.logLevel)
                .toLogLevel()

            return currentLevel >= minLevel
        }

        private fun String.toLogLevel() = when (this) {
            LogglyConstants.LogTypes.verbose -> 1
            LogglyConstants.LogTypes.debug -> 2
            LogglyConstants.LogTypes.info -> 3
            LogglyConstants.LogTypes.warn -> 4
            LogglyConstants.LogTypes.error -> 5
            else -> 5
        }
    }
}
