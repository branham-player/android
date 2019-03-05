package com.branhamplayer.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.branhamplayer.android.R
import com.branhamplayer.android.actions.PreflightChecklistAction
import com.branhamplayer.android.di.DaggerInjector
import com.branhamplayer.android.store.startupStore

class PreflightChecklistFragment : Fragment() {

    // region Fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.preflight_checklist_fragment, container, false)

        DaggerInjector.buildPreflightChecklistComponent()
        startupStore.dispatch(PreflightChecklistAction.CheckPlatformStatusAction)

        return view
    }

    // endregion
}
