package com.branhamplayer.android.di

import com.branhamplayer.android.base.di.ReducerScope
import dagger.Component

@ReducerScope
@Component(dependencies = [StartupComponent::class], modules = [RoutingModule::class])
interface RoutingComponent
