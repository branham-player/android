package com.branhamplayer.android.di

import android.app.Activity
import android.content.Context
import com.branhamplayer.android.middleware.StartupMiddleware
import com.branhamplayer.android.reducers.StartupReducer
import com.branhamplayer.android.ui.MainActivity
import dagger.Component

@Component(modules = [AuthenticationModule::class, RoutingModule::class, StartupModule::class])
interface StartupComponent {

    fun getActivity(): Activity
    fun getContext(): Context

    fun inject(activity: MainActivity)
    fun inject(rootMiddleware: StartupMiddleware)
    fun inject(rootReducer: StartupReducer)
}
