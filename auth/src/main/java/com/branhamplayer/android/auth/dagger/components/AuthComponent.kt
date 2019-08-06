package com.branhamplayer.android.auth.dagger.components

import com.branhamplayer.android.auth.dagger.modules.FirebaseModule
import com.branhamplayer.android.auth.dagger.modules.UtilsModule
import dagger.Subcomponent

@Subcomponent(modules = [
    FirebaseModule::class,
    UtilsModule::class
])
interface AuthComponent
