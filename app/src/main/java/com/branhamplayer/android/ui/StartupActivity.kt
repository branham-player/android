package com.branhamplayer.android.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.branhamplayer.android.BuildConfig
import com.branhamplayer.android.R
import com.branhamplayer.android.auth.navigation.LoginNavigator
import com.branhamplayer.android.dagger.DaggerInjector
import com.google.firebase.analytics.FirebaseAnalytics
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
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

        setUpAppCenter()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LoginNavigator.RequestCode && resultCode == Activity.RESULT_OK) {
            findNavController(R.id.startup_navigation_host)
                .navigate(R.id.action_welcome_fragment_to_sermons_module)
        }
    }

    // endregion

    private fun setUpAppCenter() {
        if (BuildConfig.DEBUG) {
            AppCenter.start(
                application,
                BuildConfig.APP_CENTER_KEY,
                Analytics::class.java
            )
        } else {
            AppCenter.start(
                application,
                BuildConfig.APP_CENTER_KEY,
                Analytics::class.java,
                Crashes::class.java
            )
        }
    }
}
