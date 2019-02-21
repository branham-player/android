package com.branhamplayer.android.di

import com.branhamplayer.android.base.di.FragmentScope
import com.branhamplayer.android.ui.AuthenticationFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [AuthenticationModule::class])
interface AuthenticationComponent {
    fun inject(fragment: AuthenticationFragment)
}
