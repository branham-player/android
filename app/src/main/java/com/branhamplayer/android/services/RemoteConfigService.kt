package com.branhamplayer.android.services

import com.branhamplayer.android.BuildConfig
import com.branhamplayer.android.R
import com.branhamplayer.android.StartupConstants
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

object RemoteConfigService {

    fun initializeInstance() {
        val settings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(BuildConfig.DEBUG)
            .build()

        val firebase = FirebaseRemoteConfig.getInstance()

        firebase.setConfigSettings(settings)
        firebase.setDefaults(R.xml.remote_config_defaults)

        firebase.fetch(StartupConstants.FirebaseRemoteConfig.cacheExipration)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    firebase.activateFetched()
                }
            }
    }
}
