package com.branhamplayer.android.di

import com.branhamplayer.android.ui.AuthenticationFragment
import dagger.Component

@Component(dependencies = [StartupComponent::class], modules = [AuthenticationModule::class])
interface AuthenticationComponent {
    fun inject(fragment: AuthenticationFragment)
}
