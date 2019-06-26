package com.branhamplayer.android.sermons.dagger

import androidx.appcompat.app.AppCompatActivity
import com.branhamplayer.android.dagger.AuthenticationModule
import com.branhamplayer.android.sermons.dagger.components.DaggerSermonsComponent
import com.branhamplayer.android.sermons.dagger.components.SermonsComponent
import com.branhamplayer.android.sermons.dagger.modules.SermonListModule
import com.branhamplayer.android.sermons.dagger.modules.SermonsModule

object DaggerInjector {

    var sermonsComponent: SermonsComponent? = null
        private set

    fun buildSermonsComponent(activity: AppCompatActivity): SermonsComponent {
        val component = sermonsComponent ?: DaggerSermonsComponent
            .builder()
            .authenticationModule(AuthenticationModule(activity))
            .sermonListModule(
                SermonListModule(
                    activity
                )
            )
            .sermonsModule(SermonsModule(activity))
            .build()

        sermonsComponent = component
        return component
    }
}
