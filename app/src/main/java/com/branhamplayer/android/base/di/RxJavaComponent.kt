package com.branhamplayer.android.base.di

import dagger.Component

@MiddlewareScope
@Component(modules = [RxJavaModule::class])
interface RxJavaComponent {

}
