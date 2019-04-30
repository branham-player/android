package com.branhamplayer.android.di

import com.branhamplayer.android.ui.WelcomeFragment
import dagger.Component

@Component(dependencies = [StartupComponent::class], modules = [AuthenticationModule::class])
interface AuthenticationComponent {
    fun inject(fragment: WelcomeFragment)
}
