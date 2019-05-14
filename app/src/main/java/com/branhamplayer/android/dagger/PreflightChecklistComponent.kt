package com.branhamplayer.android.dagger

import com.branhamplayer.android.ui.PreflightChecklistFragment
import dagger.Component

@Component(modules = [PreflightChecklistModule::class])
interface PreflightChecklistComponent {
    fun inject(fragment: PreflightChecklistFragment)
}
