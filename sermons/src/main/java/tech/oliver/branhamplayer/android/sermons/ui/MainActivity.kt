package tech.oliver.branhamplayer.android.sermons.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private val implementation = MainActivityImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        implementation.onCreate(savedInstanceState)
    }
}
