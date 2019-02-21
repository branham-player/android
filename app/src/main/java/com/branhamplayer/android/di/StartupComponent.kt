package com.branhamplayer.android.di

import com.branhamplayer.android.base.di.ActivityScope
import com.branhamplayer.android.ui.MainActivity
import dagger.Component

@ActivityScope
@Component(modules = [StartupModule::class])
interface StartupComponent {
    fun inject(activity: MainActivity)
}
