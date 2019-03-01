package com.branhamplayer.android.sermons.di

import dagger.Component

@Component(dependencies = [SermonsComponent::class], modules = [RxJavaModule::class])
interface PermissionComponent
