package tech.oliver.branhamplayer.android.files

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tech.oliver.branhamplayer.android.files.ui.filelist.FileListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, FileListFragment.newInstance())
                    .commitNow()
        }
    }
}
