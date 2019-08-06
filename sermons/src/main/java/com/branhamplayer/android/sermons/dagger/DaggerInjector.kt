package com.branhamplayer.android.sermons.dagger

import com.branhamplayer.android.dagger.modules.AuthenticationModule
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
        val component = DaggerSermonsComponent
            .builder()
            .authenticationModule(AuthenticationModule(activity))
            .databaseModule(DatabaseModule(activity))
            .sermonListModule(SermonListModule(activity))
            .sermonsModule(SermonsModule(activity))
            .build()

        sermonsComponent = component
        return component
    }
}
