package com.branhamplayer.android.dagger.components

import com.branhamplayer.android.dagger.modules.AuthenticationModule
import com.branhamplayer.android.dagger.modules.RoutingModule
import dagger.Component

@Component(modules = [AuthenticationModule::class, RoutingModule::class])
interface RoutingComponent
