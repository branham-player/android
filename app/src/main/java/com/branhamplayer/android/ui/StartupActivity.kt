package com.branhamplayer.android.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.branhamplayer.android.BuildConfig
import com.branhamplayer.android.R
import com.branhamplayer.android.dagger.DaggerInjector
import com.branhamplayer.android.store.startupStore
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class StartupActivity : AppCompatActivity() {

    // region Dagger

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    // endregion

    // region AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.startup_activity)

        DaggerInjector.buildStartupComponent(this).inject(this)
        firebaseAnalytics.setAnalyticsCollectionEnabled(BuildConfig.BUILD_TYPE.toLowerCase() == "release")

        setUpNavigationGraph()
    }

    // endregion

    private fun setUpNavigationGraph() {
        val navigationHost = if (startupStore.state.ranPreflightChecklistSuccessfully) {
            NavHostFragment.create(R.navigation.after_preflight_checklist_navigation_graph)
        } else {
            NavHostFragment.create(R.navigation.before_preflight_checklist_navigation_graph)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.startup_navigation_host, navigationHost)
            .setPrimaryNavigationFragment(navigationHost)
            .commit()
    }
}
