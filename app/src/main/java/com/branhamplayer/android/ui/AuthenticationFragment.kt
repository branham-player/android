package com.branhamplayer.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.auth0.android.authentication.storage.CredentialsManager
import com.branhamplayer.android.App
import com.branhamplayer.android.R
import com.branhamplayer.android.actions.AuthenticationAction
import com.branhamplayer.android.actions.RoutingAction
import com.branhamplayer.android.di.AuthenticationModule
import com.branhamplayer.android.di.DaggerAuthenticationComponent
import com.branhamplayer.android.store.startupStore
import javax.inject.Inject

class AuthenticationFragment : Fragment() {

    @Inject
    lateinit var credentialsManager: CredentialsManager

    private var unbinder: Unbinder? = null

    // region Fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.authentication_fragment, container, false)
        unbinder = ButterKnife.bind(this, view)

        context?.let {
            DaggerAuthenticationComponent
                .builder()
                .authenticationModule(AuthenticationModule(it))
                .build()
                .inject(this)
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        context?.let {
            if (credentialsManager.hasValidCredentials()) {
                startupStore.dispatch(RoutingAction.NavigateToSermonsAction(it))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
    }

    // endregion

    // region Event Listeners

    @OnClick(R.id.login_button)
    fun login() = launchAuth0()

    @OnClick(R.id.login_register)
    fun register() = launchAuth0()

    // endregion

    private fun launchAuth0() {
        activity?.let {
            startupStore.dispatch(AuthenticationAction.DoLoginAction(it))
        }
    }
}
