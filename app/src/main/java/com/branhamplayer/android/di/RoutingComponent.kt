package com.branhamplayer.android.di

import dagger.Component

@Component(modules = [AuthenticationModule::class, RoutingModule::class])
interface RoutingComponent
