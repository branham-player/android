package tech.oliver.branhamplayer

import android.support.v4.app.Fragment
import tech.oliver.branhamplayer.filelist.FileListFragment

class MainActivityImplementation(private val activity: MainActivity) {

    // region Lifecycle

    fun onCreate() {
        activity.setContentView(R.layout.main_activity)
        addFragment(FileListFragment(), false)
    }

    // endregion


    // region Private Methods

    private fun addFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val tag = fragment::class.java.simpleName
        val transaction = activity.supportFragmentManager?.beginTransaction()

        if (addToBackStack) {
            transaction?.addToBackStack(tag)
        }

        transaction?.replace(R.id.container, fragment, tag)
        transaction?.commit()
    }

    // endregion
}
