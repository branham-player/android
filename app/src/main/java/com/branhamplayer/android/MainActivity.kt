package com.branhamplayer.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import org.koin.standalone.StandAloneContext
import com.branhamplayer.android.shared.startupModule

class MainActivity : AppCompatActivity() {

    private val implementation: MainActivityImpl by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StandAloneContext.loadKoinModules(startupModule)
        implementation.onCreate(savedInstanceState)
    }
}
