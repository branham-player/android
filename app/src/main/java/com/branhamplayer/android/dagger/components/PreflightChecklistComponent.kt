package com.branhamplayer.android.dagger.components

import com.branhamplayer.android.dagger.modules.PreflightChecklistModule
import com.branhamplayer.android.dagger.modules.RxJavaModule
import com.branhamplayer.android.data.dagger.DatabaseModule
import com.branhamplayer.android.data.dagger.MapperModule
import com.branhamplayer.android.data.dagger.NetworkModule
import com.branhamplayer.android.ui.PreflightChecklistFragment
import dagger.Component

@Component(modules = [
    DatabaseModule::class,
    MapperModule::class,
    NetworkModule::class,
    PreflightChecklistModule::class,
    RxJavaModule::class
])
interface PreflightChecklistComponent {
    fun inject(fragment: PreflightChecklistFragment)
}
