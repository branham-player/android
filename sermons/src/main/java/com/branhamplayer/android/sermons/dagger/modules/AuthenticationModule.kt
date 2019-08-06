package com.branhamplayer.android.sermons.dagger.modules

import com.branhamplayer.android.auth.utils.User
import com.branhamplayer.android.sermons.reducers.AuthenticationReducer
import dagger.Module
import dagger.Provides

@Module
class AuthenticationModule {

    @Provides
    fun provideAuthenticationReducer(
        user: User
    ) = AuthenticationReducer(
        user = user
    )
}
