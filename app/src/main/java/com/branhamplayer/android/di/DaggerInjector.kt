package com.branhamplayer.android.di

import android.app.Activity

object DaggerInjector {

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
}
