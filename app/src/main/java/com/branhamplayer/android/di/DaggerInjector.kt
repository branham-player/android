package com.branhamplayer.android.di

import android.app.Activity

object DaggerInjector {

    // region Authentication

    var authenticationComponent: AuthenticationComponent? = null
        private set

    fun buildAuthenticationComponent(): AuthenticationComponent {
        val component = authenticationComponent ?: DaggerAuthenticationComponent
            .builder()
            .startupComponent(startupComponent)
            .build()

        authenticationComponent = component
        return component
    }

    // endregion

    // region Routing

    var routingComponent: RoutingComponent? = null
        private set

    fun buildRoutingComponent(): RoutingComponent {
        val component = routingComponent ?: DaggerRoutingComponent
            .builder()
            .startupComponent(startupComponent)
            .build()

        routingComponent = component
        return component
    }

    // endregion

    // region Startup Activity & Root Components

    var startupComponent: StartupComponent? = null
        private set

    fun buildStartupComponent(activity: Activity): StartupComponent {
        val component = startupComponent ?: DaggerStartupComponent
            .builder()
            .startupModule(StartupModule(activity))
            .build()

        startupComponent = component
        return component
    }

    // endregion
}
