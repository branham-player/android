package com.branhamplayer.android.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.branhamplayer.android.BuildConfig
import com.branhamplayer.android.R
import com.branhamplayer.android.actions.RoutingAction
import com.branhamplayer.android.di.DaggerInjector
import com.branhamplayer.android.store.startupStore
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class StartupActivity : AppCompatActivity() {

    // region DI

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    // endregion

    // region AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.startup_activity)

        DaggerInjector.buildStartupComponent(this).inject(this)

        firebaseAnalytics.setAnalyticsCollectionEnabled(BuildConfig.BUILD_TYPE.toLowerCase() == "release")
        startupStore.dispatch(RoutingAction.NavigateToPreflightChecklistAction)
    }

    // endregion

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.commit(allowStateLoss = true) {
            replace(R.id.startup_container, fragment)
        }
    }
}
