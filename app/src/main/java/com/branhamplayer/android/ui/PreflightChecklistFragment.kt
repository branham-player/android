package com.branhamplayer.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.branhamplayer.android.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import javax.inject.Inject

class PreflightChecklistFragment : Fragment() {

    // region DI

    @Inject
    lateinit var firebaseConfig: FirebaseRemoteConfig

    // endregion

    // region Fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.preflight_checklist_fragment, container, false)

    // endregion
}
