package com.branhamplayer.android.auth.dagger.components

import com.branhamplayer.android.auth.dagger.modules.UtilsModule
import dagger.Component

@Component(modules = [
    UtilsModule::class
])
interface AuthComponent
