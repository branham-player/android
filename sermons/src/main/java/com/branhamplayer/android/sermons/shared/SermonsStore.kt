package com.branhamplayer.android.sermons.shared

import com.branhamplayer.android.sermons.middleware.PermissionMiddleware
import com.branhamplayer.android.sermons.middleware.ProfileMiddleware
import com.branhamplayer.android.sermons.reducers.SermonsReducer
import org.rekotlin.Store

val sermonsStore = Store(
        reducer = SermonsReducer.Companion::reduce,
        state = null,
        middleware = listOf(
                (PermissionMiddleware.Companion::process)(),
                (ProfileMiddleware.Companion::process)()
        )
)
