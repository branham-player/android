package tech.oliver.branhamplayer.android.startup.shared

import org.rekotlin.Store
import tech.oliver.branhamplayer.android.startup.middleware.AuthenticationMiddleware
import tech.oliver.branhamplayer.android.startup.reducers.StartupReducer

val startupStore = Store(
        reducer = StartupReducer.Companion::reduce,
        state = null,
        middleware = listOf((AuthenticationMiddleware.Companion::process)())
)
