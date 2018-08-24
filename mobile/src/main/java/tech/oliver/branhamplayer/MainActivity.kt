package tech.oliver.branhamplayer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.rekotlin.Store
import tech.oliver.branhamplayer.root.AppState
import tech.oliver.branhamplayer.root.appReducer

val store: Store<AppState> = Store(::appReducer, null)

class MainActivity : AppCompatActivity() {

    val implementation = MainActivityImplementation(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        implementation.onCreate()
    }
}
