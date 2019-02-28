package com.branhamplayer.android.di

import com.branhamplayer.android.base.di.MiddlewareScope
import com.branhamplayer.android.middleware.StartupMiddleware
import dagger.Component

@MiddlewareScope
@Component(modules = [MiddlewareModule::class])
interface MiddlewareComponent {
    fun inject(middleware: StartupMiddleware)
}