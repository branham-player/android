package com.branhamplayer.android.di

import com.branhamplayer.android.ui.AuthenticationFragment
import dagger.Subcomponent

@Subcomponent(modules = [AuthenticationModule::class])
interface AuthenticationComponent {
    fun inject(fragment: AuthenticationFragment)
}
