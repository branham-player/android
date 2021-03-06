package com.branhamplayer.android.sermons.dagger.modules

import com.branhamplayer.android.dagger.RxJavaModule
import com.branhamplayer.android.sermons.middleware.AuthenticationMiddleware
import com.branhamplayer.android.sermons.reducers.AuthenticationReducer
import com.branhamplayer.android.utils.auth0.ProfileManager
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Named

@Module
class AuthenticationModule {

    @Provides
    fun provideAuthenticationMiddleware(
        profileManager: ProfileManager,
        @Named(RxJavaModule.BG) bg: Scheduler,
        @Named(RxJavaModule.UI) ui: Scheduler
    ) = AuthenticationMiddleware(profileManager, bg, ui)

    @Provides
    fun provideAuthenticationReducer() = AuthenticationReducer()
}
