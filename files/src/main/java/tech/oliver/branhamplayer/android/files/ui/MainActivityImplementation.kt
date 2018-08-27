package tech.oliver.branhamplayer.android.files.ui

import android.os.Bundle
import androidx.fragment.app.transaction
import tech.oliver.branhamplayer.android.files.R

class MainActivityImplementation(private val mainActivity: MainActivity) {

    fun onCreate(savedInstanceState: Bundle?) {
        mainActivity.setContentView(R.layout.main_activity)

        if (savedInstanceState != null) return

        mainActivity.supportFragmentManager.transaction(now = true, allowStateLoss = false) {
            replace(R.id.container, FileListFragment.newInstance())
        }
    }
}
