package com.branhamplayer.android.sermons.di

import androidx.appcompat.app.AppCompatActivity

object DaggerInjector {

    // region Permissions

    var permissionComponent: PermissionComponent? = null
        private set

    fun buildPermissionComponent(): PermissionComponent {
        val component = permissionComponent ?: DaggerPermissionComponent
            .builder()
            .sermonsComponent(sermonsComponent)
            .build()

        permissionComponent = component
        return component
    }

    // endregion

    // region RxJava

    var rxJavaComponent: RxJavaComponent? = null
        private set

    fun buildRxJavaComponent(): RxJavaComponent {
        val component = rxJavaComponent ?: DaggerRxJavaComponent
            .builder()
            .build()

        rxJavaComponent = component
        return component
    }

    // endregion

    // region Sermons Activity & Root Components

    var sermonsComponent: SermonsComponent? = null
        private set

    fun buildSermonsComponent(activity: AppCompatActivity): SermonsComponent {
        val component = sermonsComponent ?: DaggerSermonsComponent
            .builder()
            .sermonsModule(SermonsModule(activity))
            .build()

        sermonsComponent = component
        return component
    }

    // endregion
}