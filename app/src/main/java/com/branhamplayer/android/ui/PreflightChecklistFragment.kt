package com.branhamplayer.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.branhamplayer.android.R
import com.branhamplayer.android.actions.PreflightChecklistAction
import com.branhamplayer.android.actions.RoutingAction
import com.branhamplayer.android.di.DaggerInjector
import com.branhamplayer.android.states.StartupState
import com.branhamplayer.android.store.startupStore
import org.rekotlin.StoreSubscriber
import javax.inject.Inject

class PreflightChecklistFragment : Fragment(), StoreSubscriber<StartupState> {

    // region DI

    @Inject
    lateinit var alertDialogBuilder: AlertDialog.Builder

    // endregion

    // region Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let {
            DaggerInjector.buildPreflightChecklistComponent(it).inject(this)
        }

        startupStore.subscribe(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.preflight_checklist_fragment, container, false)

    override fun onDestroy() {
        super.onDestroy()
        startupStore.unsubscribe(this)
    }

    override fun onResume() {
        super.onResume()
        startupStore.dispatch(PreflightChecklistAction.CheckPlatformStatusAction)
    }

    // endregion

    // region StoreSubscriber

    override fun newState(state: StartupState) {
        if (!state.platformAvailable) {
            showPlatformUnavailableMessage(state.message)
            return
        }

        if (!state.minimumVersionMet) {
            showMinimumVersionNotMetMessage()
            return
        }

        if (!state.message.isNullOrBlank()) {
            showNotice(state.message)
        }
    }

    // endregion

    private fun showMinimumVersionNotMetMessage() {
        val builder = alertDialogBuilder
            .setCancelable(false)
            .setMessage(R.string.preflight_checklist_required_update_message)
            .setTitle(R.string.preflight_checklist_required_update_title)
            .setPositiveButton(R.string.preflight_checklist_update_app) { dialog, _ ->
                dialog.dismiss()

            }
            .setNegativeButton(R.string.preflight_checklist_close_app) { dialog, _ ->
                dialog.dismiss()
                startupStore.dispatch(RoutingAction.CloseAppAction)
            }

        builder.create().show()
    }

    private fun showNotice(message: String) {
        val builder = alertDialogBuilder
            .setCancelable(false)
            .setMessage(message)
            .setTitle(R.string.preflight_checklist_notice)
            .setPositiveButton(R.string.preflight_checklist_ok) { dialog, _ ->
                dialog.dismiss()
                startupStore.dispatch(RoutingAction.NavigateToAuthenticationAction)
            }

        builder.create().show()
    }

    private fun showPlatformUnavailableMessage(message: String?) {
        val builder = alertDialogBuilder
            .setCancelable(false)
            .setTitle(R.string.preflight_checklist_platform_unavailable_title)
            .setPositiveButton(R.string.preflight_checklist_close_app) { dialog, _ ->
                dialog.dismiss()
                startupStore.dispatch(RoutingAction.CloseAppAction)
            }

        if (message.isNullOrBlank()) {
            builder.setMessage(R.string.preflight_checklist_platform_unavailable_message)
        } else {
            builder.setMessage(message)
        }

        builder.create().show()
    }
}
