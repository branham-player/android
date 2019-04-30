package com.branhamplayer.android.sermons.di

import androidx.appcompat.app.AppCompatActivity
import com.branhamplayer.android.di.AuthenticationModule

object DaggerInjector {

    var sermonsComponent: SermonsComponent? = null
        private set

    fun buildSermonsComponent(activity: AppCompatActivity): SermonsComponent {
        val component = sermonsComponent ?: DaggerSermonsComponent
            .builder()
            .authenticationModule(AuthenticationModule(activity))
            .rxJavaModule(RxJavaModule())
            .sermonsModule(SermonsModule(activity))
            .build()

        sermonsComponent = component
        return component
    }
}
