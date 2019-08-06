package com.branhamplayer.android.sermons.dagger

import com.branhamplayer.android.auth.dagger.components.DaggerAuthComponent
import com.branhamplayer.android.data.dagger.DatabaseModule
import com.branhamplayer.android.sermons.dagger.components.DaggerSermonsComponent
import com.branhamplayer.android.sermons.dagger.components.SermonsComponent
import com.branhamplayer.android.sermons.dagger.modules.SermonListModule
import com.branhamplayer.android.sermons.dagger.modules.SermonsModule
import com.branhamplayer.android.sermons.ui.SermonsActivity

object DaggerInjector {

    var sermonsComponent: SermonsComponent? = null
        private set

    fun buildSermonsComponent(activity: SermonsActivity): SermonsComponent {
        val authComponent = DaggerAuthComponent
            .builder()
            .build()

        val component = DaggerSermonsComponent
            .builder()
            .authComponent(authComponent)
            .databaseModule(DatabaseModule(activity))
            .sermonListModule(SermonListModule(activity))
            .sermonsModule(SermonsModule(activity))
            .build()

        sermonsComponent = component
        return component
    }
}
