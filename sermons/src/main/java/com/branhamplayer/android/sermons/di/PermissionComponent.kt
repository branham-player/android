package com.branhamplayer.android.sermons.di

import com.branhamplayer.android.base.di.MiddlewareScope
import dagger.Component

@MiddlewareScope
@Component(modules = [RxJavaModule::class])
interface PermissionComponent
