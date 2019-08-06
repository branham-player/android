package com.branhamplayer.android.dagger.components

import com.branhamplayer.android.auth.dagger.components.AuthComponent
import com.branhamplayer.android.dagger.modules.AuthenticationModule
import com.branhamplayer.android.ui.WelcomeFragment
import dagger.Component

@Component(modules = [
    AuthenticationModule::class
], dependencies = [
    AuthComponent::class
])
interface WelcomeComponent {
    fun inject(fragment: WelcomeFragment)
}
