package com.branhamplayer.android.dagger

import com.branhamplayer.android.ui.WelcomeFragment
import dagger.Component

@Component(modules = [AuthenticationModule::class])
interface WelcomeComponent {
    fun inject(fragment: WelcomeFragment)
}
