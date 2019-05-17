package com.branhamplayer.android.dagger

import com.branhamplayer.android.data.dagger.DatabaseModule
import com.branhamplayer.android.data.dagger.MapperModule
import com.branhamplayer.android.data.dagger.NetworkModule
import com.branhamplayer.android.ui.PreflightChecklistFragment
import dagger.Component

@Component(
    modules = [
        DatabaseModule::class,
        MapperModule::class,
        NetworkModule::class,
        PreflightChecklistModule::class,
        RxJavaModule::class
    ]
)
interface PreflightChecklistComponent {
    fun inject(fragment: PreflightChecklistFragment)
}
