package com.branhamplayer.android.auth.dagger.modules

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {

    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()
}