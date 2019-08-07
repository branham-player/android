package com.branhamplayer.android.dagger.components

import com.branhamplayer.android.dagger.modules.RoutingModule
import dagger.Component

@Component(modules = [
    RoutingModule::class
])
interface RoutingComponent
