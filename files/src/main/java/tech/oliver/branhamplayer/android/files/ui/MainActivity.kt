package tech.oliver.branhamplayer.android.files.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private val implementation = MainActivityImplementation(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        implementation.onCreate(savedInstanceState)
    }
}
