package com.branhamplayer.android.di

import android.app.Activity

class DaggerInjector {
    companion object {

        var authenticationComponent: AuthenticationComponent? = null
            private set


        fun buildAuthenticationComponent(activity: Activity): AuthenticationComponent  {
            val component = authenticationComponent ?: DaggerAuthenticationComponent
                .builder()
                .authenticationModule(AuthenticationModule(activity))
                .build()

            authenticationComponent = component
            return component
        }
    }
}
