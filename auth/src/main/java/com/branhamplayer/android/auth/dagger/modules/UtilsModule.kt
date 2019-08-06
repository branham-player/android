package com.branhamplayer.android.auth.dagger.modules

import com.branhamplayer.android.auth.utils.User
import dagger.Module
import dagger.Provides

@Module
class UtilsModule {

    @Provides
    fun provideUser() = User()
}