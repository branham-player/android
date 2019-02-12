package com.branhamplayer.android.sermons.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.branhamplayer.android.sermons.R
import com.branhamplayer.android.sermons.shared.sermonsModule
import org.koin.android.ext.android.inject
import org.koin.standalone.StandAloneContext

class SermonsActivity : AppCompatActivity() {

    private val sermonListFragment: SermonListFragment by inject()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        StandAloneContext.loadKoinModules(sermonsModule)
        setContentView(R.layout.sermons_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        supportFragmentManager.commit(allowStateLoss = true) {
            replace(R.id.sermon_list_container, sermonListFragment)
        }
    }
}
