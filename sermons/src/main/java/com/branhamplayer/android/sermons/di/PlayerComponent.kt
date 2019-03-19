package com.branhamplayer.android.sermons.di

import com.branhamplayer.android.sermons.ui.PlayerFragment
import dagger.Component

@Component(modules = [PlayerModule::class])
interface PlayerComponent {
    fun inject(fragment: PlayerFragment)
}
