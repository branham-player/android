package com.branhamplayer.android.di

import com.branhamplayer.android.ui.PreflightChecklistFragment
import dagger.Component

@Component(modules = [PreflightChecklistModule::class])
interface PreflightChecklistComponent {
    fun inject(fragment: PreflightChecklistFragment)
}
