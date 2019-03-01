package com.branhamplayer.android.sermons.di

import com.branhamplayer.android.sermons.middleware.SermonsMiddleware
import dagger.Component

@Component(modules = [SermonsModule::class])
interface SermonsComponent {

    fun inject(rootMiddleware: SermonsMiddleware)
}
