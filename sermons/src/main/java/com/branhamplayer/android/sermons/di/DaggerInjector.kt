package com.branhamplayer.android.sermons.di

import androidx.appcompat.app.AppCompatActivity
import com.branhamplayer.android.di.AuthenticationModule

object DaggerInjector {

    // region Sermons Activity & Root Components

    var sermonsComponent: SermonsComponent? = null
        private set

    fun buildSermonsComponent(activity: AppCompatActivity): SermonsComponent {
        val component = sermonsComponent ?: DaggerSermonsComponent
            .builder()
            .authenticationModule(AuthenticationModule())
            .rxJavaModule(RxJavaModule())
            .sermonsModule(SermonsModule(activity))
            .build()

        sermonsComponent = component
        return component
    }

    // endregion
}