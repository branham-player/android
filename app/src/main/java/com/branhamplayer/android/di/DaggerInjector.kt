package com.branhamplayer.android.di

import android.app.Activity
import android.content.Context

class DaggerInjector {
    companion object {

        // region Authentication

        var authenticationComponent: AuthenticationComponent? = null
            private set

        fun buildAuthenticationComponent(activity: Activity): AuthenticationComponent {
            val component = authenticationComponent ?: DaggerAuthenticationComponent
                .builder()
                .authenticationModule(AuthenticationModule(activity))
                .build()

            authenticationComponent = component
            return component
        }

        // endregion

        // region Routing

        var routingComponent: RoutingComponent? = null
            private set

        fun buildRoutingComponent(context: Context): RoutingComponent {
            val component = routingComponent ?: DaggerRoutingComponent
                .builder()
                .routingModule(RoutingModule(context))
                .build()

            routingComponent = component
            return component
        }

        // endregion

        // region Startup Activity & Module-wide Components

        var startupComponent: StartupComponent? = null
            private set

        fun buildStartupComponent(activity: Activity): StartupComponent {
            val component = startupComponent ?: DaggerStartupComponent
                .builder()
                .authenticationModule(AuthenticationModule(activity))
                .startupModule(StartupModule())
                .build()

            startupComponent = component
            return component
        }

        // endregion
    }
}
