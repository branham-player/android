package com.branhamplayer.android.di

import android.app.Activity

object DaggerInjector {

    // region Authentication

    var authenticationComponent: AuthenticationComponent? = null
        private set

    fun buildAuthenticationComponent(): AuthenticationComponent {
        val component = authenticationComponent ?: DaggerAuthenticationComponent
            .builder()
            .authenticationModule(AuthenticationModule())
            .startupComponent(startupComponent)
            .build()

        authenticationComponent = component
        return component
    }

    // endregion

    // region Preflight Checklist

    var preflightChecklistComponent: PreflightChecklistComponent? = null
        private set

    fun buildPreflightChecklistComponent(): PreflightChecklistComponent {
        val component = preflightChecklistComponent ?: DaggerPreflightChecklistComponent
            .builder()
            .build()

        preflightChecklistComponent = component
        return component
    }

    // endregion

    // region Activity & Module-wide Injections

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
