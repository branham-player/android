package com.branhamplayer.android.sermons.dagger

import com.branhamplayer.android.dagger.AuthenticationModule
import com.branhamplayer.android.sermons.middleware.SermonsMiddleware
import com.branhamplayer.android.sermons.reducers.SermonsReducer
import com.branhamplayer.android.sermons.ui.SermonListFragment
import com.branhamplayer.android.sermons.ui.SermonsActivity
import dagger.Component

@Component(modules = [AuthenticationModule::class, DrawerModule::class, RxJavaModule::class, SermonListModule::class, SermonsModule::class])
interface SermonsComponent {
    fun inject(activity: SermonsActivity)
    fun inject(fragment: SermonListFragment)
    fun inject(rootMiddleware: SermonsMiddleware)
    fun inject(rootReducer: SermonsReducer)
}
