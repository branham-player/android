package com.branhamplayer.android.dagger

import android.content.Context
import com.branhamplayer.android.auth.dagger.components.DaggerAuthComponent
import com.branhamplayer.android.dagger.components.DaggerPreflightChecklistComponent
import com.branhamplayer.android.dagger.components.DaggerStartupComponent
import com.branhamplayer.android.dagger.components.DaggerWelcomeComponent
import com.branhamplayer.android.dagger.components.PreflightChecklistComponent
import com.branhamplayer.android.dagger.components.StartupComponent
import com.branhamplayer.android.dagger.components.WelcomeComponent
import com.branhamplayer.android.dagger.modules.PreflightChecklistModule
import com.branhamplayer.android.dagger.modules.RoutingModule
import com.branhamplayer.android.dagger.modules.StartupModule
import com.branhamplayer.android.data.dagger.DatabaseModule
import com.branhamplayer.android.ui.StartupActivity

object DaggerInjector {

    // region Preflight Checklist

    // This component needs to be rebuilt every time. Since the preflight checklist fragment
    // is capable of shutting the app down (and thus invalidating the given context), it
    // needs to receive a fresh context each time so that the dialog which depends on it can
    // show a dialog with a valid context without crashing the app.
    fun buildPreflightChecklistComponent(context: Context): PreflightChecklistComponent =
        DaggerPreflightChecklistComponent
            .builder()
            .databaseModule(DatabaseModule(context))
            .preflightChecklistModule(PreflightChecklistModule(context))
            .build()

    // endregion

    // region Activity & Module-wide Injections

    var startupComponent: StartupComponent? = null
        private set

    fun buildStartupComponent(activity: StartupActivity): StartupComponent {
        val component = startupComponent ?: DaggerStartupComponent
            .builder()
            .databaseModule(DatabaseModule(activity))
            .preflightChecklistModule(PreflightChecklistModule(activity))
            .routingModule(RoutingModule(activity))
            .startupModule(StartupModule(activity))
            .build()

        startupComponent = component
        return component
    }

    // endregion

    // region Welcome

    var welcomeComponent: WelcomeComponent? = null
        private set

    fun buildWelcomeComponent(): WelcomeComponent {
        val authComponent = DaggerAuthComponent
            .builder()
            .build()

        val component = welcomeComponent ?: DaggerWelcomeComponent
            .builder()
            .authComponent(authComponent)
            .build()

        welcomeComponent = component
        return component
    }

    // endregion
}
