package com.branhamplayer.android.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.branhamplayer.android.App
import com.branhamplayer.android.R
import com.branhamplayer.android.base.di.ApplicationComponent
import com.branhamplayer.android.di.StartupModule
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var authenticationFragment: AuthenticationFragment

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        getApplicationComponent()
            .newStartupComponent(StartupModule())
            .inject(this)

        supportFragmentManager.commit(allowStateLoss = true) {
            replace(R.id.start_up_container, authenticationFragment)
        }
    }

    private fun getApplicationComponent(): ApplicationComponent {
        val app = application as App

        return app.getApplicationComponent()
    }
}
