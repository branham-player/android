package com.branhamplayer.android.sermons.di

import androidx.appcompat.app.AppCompatActivity
import com.branhamplayer.android.di.AuthenticationModule

object DaggerInjector {

    // region Permissions

    var permissionComponent: PermissionComponent? = null
        private set

    fun buildPermissionComponent(): PermissionComponent {
        val component = permissionComponent ?: DaggerPermissionComponent
            .builder()
            .build()

        permissionComponent = component
        return component
    }

    // endregion

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