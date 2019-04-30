package com.branhamplayer.android.di

import dagger.Component

@Component(dependencies = [AuthenticationComponent::class, StartupComponent::class], modules = [RoutingModule::class])
interface RoutingComponent
