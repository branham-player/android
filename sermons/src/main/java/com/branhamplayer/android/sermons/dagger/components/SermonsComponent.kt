package com.branhamplayer.android.sermons.dagger.components

import com.branhamplayer.android.dagger.modules.AuthenticationModule
import com.branhamplayer.android.dagger.modules.RxJavaModule
import com.branhamplayer.android.data.dagger.DatabaseModule
import com.branhamplayer.android.sermons.dagger.modules.DrawerModule
import com.branhamplayer.android.sermons.dagger.modules.RoutingModule
import com.branhamplayer.android.sermons.dagger.modules.SermonListModule
import com.branhamplayer.android.sermons.dagger.modules.SermonsModule
import com.branhamplayer.android.sermons.middleware.SermonsMiddleware
import com.branhamplayer.android.sermons.reducers.SermonsReducer
import com.branhamplayer.android.sermons.ui.SermonListFragment
import com.branhamplayer.android.sermons.ui.SermonsActivity
import dagger.Component

@Component(modules = [
    AuthenticationModule::class,
    DatabaseModule::class,
    DrawerModule::class,
    RoutingModule::class,
    RxJavaModule::class,
    SermonListModule::class,
    SermonsModule::class
])
interface SermonsComponent {
    fun inject(activity: SermonsActivity)
    fun inject(fragment: SermonListFragment)
    fun inject(rootMiddleware: SermonsMiddleware)
    fun inject(rootReducer: SermonsReducer)
}
