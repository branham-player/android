package com.branhamplayer.android.utils

import com.branhamplayer.android.BuildConfig
import com.branhamplayer.android.R
import com.branhamplayer.android.StartupConstants
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

object RemoteConfig {

    fun initializeInstance() {
        val settings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(BuildConfig.DEBUG)
            .setMinimumFetchIntervalInSeconds(StartupConstants.FirebaseRemoteConfig.cacheExipration)
            .build()

        val firebase = FirebaseRemoteConfig.getInstance()
        firebase.setConfigSettings(settings)
        firebase.setDefaults(R.xml.remote_config_defaults)

        firebase.fetchAndActivate()
    }
}
