package tech.oliver.branhamplayer.android.sermons.shared

import org.rekotlin.Store
import tech.oliver.branhamplayer.android.sermons.middleware.PermissionMiddleware
import tech.oliver.branhamplayer.android.sermons.middleware.ProfileMiddleware
import tech.oliver.branhamplayer.android.sermons.reducers.SermonsReducer

val sermonsStore = Store(
        reducer = SermonsReducer.Companion::reduce,
        state = null,
        middleware = listOf(
                (PermissionMiddleware.Companion::process)(),
                (ProfileMiddleware.Companion::process)()
        )
)
