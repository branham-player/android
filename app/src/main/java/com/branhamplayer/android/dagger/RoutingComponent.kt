package com.branhamplayer.android.dagger

import dagger.Component

@Component(modules = [AuthenticationModule::class, RoutingModule::class])
interface RoutingComponent
