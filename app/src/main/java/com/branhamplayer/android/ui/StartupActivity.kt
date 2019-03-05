package com.branhamplayer.android.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.branhamplayer.android.R
import com.branhamplayer.android.di.DaggerInjector
import javax.inject.Inject

class StartupActivity : AppCompatActivity() {

    @Inject
    lateinit var authenticationFragment: AuthenticationFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        DaggerInjector.buildStartupComponent(this).inject(this)

        supportFragmentManager.commit(allowStateLoss = true) {
            replace(R.id.start_up_container, authenticationFragment)
        }
    }
}