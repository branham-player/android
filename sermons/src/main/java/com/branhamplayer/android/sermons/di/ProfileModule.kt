package com.branhamplayer.android.sermons.di

import com.branhamplayer.android.sermons.middleware.ProfileMiddleware
import com.branhamplayer.android.sermons.reducers.ProfileReducer
import com.branhamplayer.android.services.auth0.Auth0Service
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Named

@Module
class ProfileModule {

    @Provides
    fun provideProfileMiddleware(
        auth0Service: Auth0Service, @Named(RxJavaModule.BG) bg: Scheduler, @Named(RxJavaModule.UI) ui: Scheduler
    ) = ProfileMiddleware(auth0Service, bg, ui)

    @Provides
    fun provideProfileReducer() = ProfileReducer()
}
