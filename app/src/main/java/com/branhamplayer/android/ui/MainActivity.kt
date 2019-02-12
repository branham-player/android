package com.branhamplayer.android.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.branhamplayer.android.R
import org.koin.standalone.StandAloneContext
import com.branhamplayer.android.shared.startupModule
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val authenticationFragment: AuthenticationFragment by inject()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        StandAloneContext.loadKoinModules(startupModule)
        setContentView(R.layout.main_activity)

        supportFragmentManager.commit(allowStateLoss = true) {
            replace(R.id.start_up_container, authenticationFragment)
        }
    }
}
