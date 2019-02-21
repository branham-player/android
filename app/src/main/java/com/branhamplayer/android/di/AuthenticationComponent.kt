package com.branhamplayer.android.di

import com.branhamplayer.android.base.di.FragmentScope
import com.branhamplayer.android.base.di.MiddlewareScope
import com.branhamplayer.android.middleware.AuthenticationMiddleware
import com.branhamplayer.android.ui.AuthenticationFragment
import dagger.Component

@FragmentScope
@MiddlewareScope
@Component(modules = [AuthenticationModule::class])
interface AuthenticationComponent {
    fun inject(fragment: AuthenticationFragment)
    fun inject(middleware: AuthenticationMiddleware)
}
