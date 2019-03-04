package com.branhamplayer.android.sermons.store

import com.branhamplayer.android.sermons.middleware.SermonsMiddleware
import com.branhamplayer.android.sermons.reducers.SermonsReducer
import org.rekotlin.Store

val sermonsStore = Store(
    reducer = SermonsReducer(),
    state = null,
    middleware = listOf(SermonsMiddleware())
)
