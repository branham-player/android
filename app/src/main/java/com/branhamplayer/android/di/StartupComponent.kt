package com.branhamplayer.android.di

import com.branhamplayer.android.ui.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = [StartupModule::class])
interface StartupComponent {
    fun inject(activity: MainActivity)
}
