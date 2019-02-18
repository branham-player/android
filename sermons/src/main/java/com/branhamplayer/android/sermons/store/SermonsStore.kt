package com.branhamplayer.android.sermons.store

import com.branhamplayer.android.sermons.middleware.PermissionMiddleware
import com.branhamplayer.android.sermons.middleware.ProfileMiddleware
import com.branhamplayer.android.sermons.reducers.SermonsReducer
import org.rekotlin.Store

val sermonsStore = Store(
    reducer = SermonsReducer(),
    state = null,
    middleware = listOf(
        PermissionMiddleware(),
        ProfileMiddleware()
    )
)
