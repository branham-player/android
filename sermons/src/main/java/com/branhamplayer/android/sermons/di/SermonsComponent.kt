package com.branhamplayer.android.sermons.di

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.branhamplayer.android.di.AuthenticationModule
import com.branhamplayer.android.sermons.middleware.SermonsMiddleware
import com.branhamplayer.android.sermons.reducers.SermonsReducer
import dagger.Component

@Component(modules = [AuthenticationModule::class, DataModule::class, DrawerModule::class, ProfileModule::class, RxJavaModule::class, SermonsModule::class])
interface SermonsComponent {

    fun getActivity(): AppCompatActivity
    fun getContext(): Context

    fun inject(rootMiddleware: SermonsMiddleware)
    fun inject(rootReducer: SermonsReducer)
}
