package tech.oliver.branhamplayer.android.services.logging

import com.orhanobut.logger.Logger
import timber.log.Timber

class Loggly {

    companion object {
        fun d(tag: LogglyConstants.Tags, message: String, vararg args: Any) {
            Timber.tag("${tag.tagName}-${LogglyConstants.environment}")
            Timber.d(message, args)
            Logger.d(message, args)
        }

        fun e(tag: LogglyConstants.Tags, message: String, vararg args: Any) {
            Timber.tag("${tag.tagName}-${LogglyConstants.environment}")
            Timber.e(message, args)
            Logger.e(message, args)
        }

        fun e(tag: LogglyConstants.Tags, throwable: Throwable, message: String, vararg args: Any) {
            Timber.tag("${tag.tagName}-${LogglyConstants.environment}")
            Timber.e(throwable, message, args)
            Logger.e(throwable, message, args)
        }

        fun i(tag: LogglyConstants.Tags, message: String, vararg args: Any) {
            Timber.tag("${tag.tagName}-${LogglyConstants.environment}")
            Timber.i(message, args)
            Logger.i(message, args)
        }

        fun v(tag: LogglyConstants.Tags, message: String, vararg args: Any) {
            Timber.tag("${tag.tagName}-${LogglyConstants.environment}")
            Timber.v(message, args)
            Logger.v(message, args)
        }

        fun w(tag: LogglyConstants.Tags, message: String, vararg args: Any) {
            Timber.tag("${tag.tagName}-${LogglyConstants.environment}")
            Timber.w(message, args)
            Logger.w(message, args)
        }
    }
}
