package com.branhamplayer.android.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.branhamplayer.android.R
import com.branhamplayer.android.di.DaggerInjector
import com.branhamplayer.android.di.DaggerStartupComponent
import com.branhamplayer.android.di.StartupModule
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    @JvmField
    var authenticationFragment: AuthenticationFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        DaggerInjector.buildStartupComponent(this).inject(this)

        supportFragmentManager.commit(allowStateLoss = true) {
            authenticationFragment?.let {
                replace(R.id.start_up_container, it)
            }
        }
    }
}
