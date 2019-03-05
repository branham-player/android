package com.branhamplayer.android.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.branhamplayer.android.R
import com.branhamplayer.android.di.DaggerInjector
import javax.inject.Inject

class StartupActivity : AppCompatActivity() {

    // region DI

    @Inject
    lateinit var preflightChecklistFragment: PreflightChecklistFragment

    // endregion

    // region AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.startup_activity)

        DaggerInjector.buildStartupComponent(this).inject(this)
        setFragment(preflightChecklistFragment)
    }

    // endregion

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.commit(allowStateLoss = true) {
            replace(R.id.startup_container, fragment)
        }
    }
}
