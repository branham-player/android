package com.branhamplayer.android.sermons.di

import com.branhamplayer.android.sermons.middleware.ProfileMiddleware
import com.branhamplayer.android.sermons.reducers.ProfileReducer
import com.branhamplayer.android.utils.auth0.ProfileManager
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Named

@Module
class ProfileModule {

    @Provides
    fun provideProfileMiddleware(
        profileManager: ProfileManager, @Named(RxJavaModule.BG) bg: Scheduler, @Named(RxJavaModule.UI) ui: Scheduler
    ) = ProfileMiddleware(profileManager, bg, ui)

    @Provides
    fun provideProfileReducer() = ProfileReducer()
}
