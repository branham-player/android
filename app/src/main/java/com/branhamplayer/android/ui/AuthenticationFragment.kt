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
import com.branhamplayer.android.R
import com.branhamplayer.android.actions.AuthenticationAction
import com.branhamplayer.android.actions.RoutingAction
import com.branhamplayer.android.shared.startupStore
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.get

class AuthenticationFragment : Fragment(), KoinComponent {

    private var unbinder: Unbinder? = null

    // region Fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.authentication_fragment, container, false)
        unbinder = ButterKnife.bind(this, view)

        return view
    }

    override fun onResume() {
        super.onResume()

        context?.let {
            val userCredentials: CredentialsManager = get { parametersOf(it) }

            if (userCredentials.hasValidCredentials()) {
                startupStore.dispatch(RoutingAction.NavigateToSermonsAction(it))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
    }

    // endregion

    @OnClick(R.id.login_button)
    fun login() = launchAuth0()

    @OnClick(R.id.login_register)
    fun register() = launchAuth0()

    private fun launchAuth0() {
        activity?.let {
            startupStore.dispatch(AuthenticationAction.DoLoginAction(it))
        }
    }
}
