package com.branhamplayer.android.store

import com.branhamplayer.android.middleware.AuthenticationMiddleware
import com.branhamplayer.android.reducers.StartupReducer
import org.rekotlin.Store

val startupStore = Store(
    reducer = StartupReducer(),
    state = null,
    middleware = listOf(AuthenticationMiddleware())
)
