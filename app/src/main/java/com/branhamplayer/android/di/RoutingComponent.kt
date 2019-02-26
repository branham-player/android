package com.branhamplayer.android.di

import com.branhamplayer.android.base.di.ReducerScope
import com.branhamplayer.android.reducers.RoutingReducer
import dagger.Component

@ReducerScope
@Component(modules = [RoutingModule::class])
interface RoutingComponent {
    fun inject(fragment: RoutingReducer)
}
