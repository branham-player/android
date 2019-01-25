package com.branhamplayer.android.shared

import com.branhamplayer.android.middleware.AuthenticationMiddleware
import com.branhamplayer.android.reducers.StartupReducer
import org.rekotlin.Store

val startupStore = Store(
        reducer = StartupReducer.Companion::reduce,
        state = null,
        middleware = listOf((AuthenticationMiddleware.Companion::process)())
)
