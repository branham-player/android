package com.branhamplayer.android.di

import android.app.Activity
import android.content.Context

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

    // region Preflight Checklist

    // This component needs to be rebuilt every time. Since the preflight checklist fragment
    // is capable of shutting the app down (and thus invalidating the given context), it
    // needs to receive a fresh context each time so that the dialog which depends on it can
    // show a dialog with a valid context without crashing the app.
    fun buildPreflightChecklistComponent(context: Context) = DaggerPreflightChecklistComponent
        .builder()
        .preflightChecklistModule(PreflightChecklistModule(context))
        .build()

    // endregion

    // region Activity & Module-wide Injections

    var startupComponent: StartupComponent? = null
        private set

    fun buildStartupComponent(activity: Activity): StartupComponent {
        val component = startupComponent ?: DaggerStartupComponent
            .builder()
            .preflightChecklistModule(PreflightChecklistModule(activity))
            .startupModule(StartupModule(activity))
            .build()

        startupComponent = component
        return component
    }

    // endregion
}
