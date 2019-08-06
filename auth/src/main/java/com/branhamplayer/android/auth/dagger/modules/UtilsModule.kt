package com.branhamplayer.android.auth.dagger.modules

import com.branhamplayer.android.auth.utils.User
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

@Module
class UtilsModule {

    @Provides
    fun provideUser(
        auth: FirebaseAuth
    ) = User(
        auth = auth
    )
}