package com.branhamplayer.android.sermons.di

import com.branhamplayer.android.base.di.MiddlewareScope
import dagger.Component

@MiddlewareScope
@Component(modules = [com.branhamplayer.android.sermons.di.RxJavaModule::class])
interface RxJavaComponent {

}
