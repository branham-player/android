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
import com.branhamplayer.android.actions.RoutingAction
import com.branhamplayer.android.di.DaggerInjector
import com.branhamplayer.android.store.startupStore
import javax.inject.Inject

class WelcomeFragment : Fragment() {

    private var unbinder: Unbinder? = null

    // region Dagger

    @Inject
    lateinit var credentialsManager: CredentialsManager

    // endregion

    // region Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let {
            DaggerInjector.buildWelcomeComponent(it).inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.welcome_fragment, container, false)
        unbinder = ButterKnife.bind(this, view)

        return view
    }

    override fun onResume() {
        super.onResume()

        if (credentialsManager.hasValidCredentials()) {
            startupStore.dispatch(RoutingAction.NavigateToSermonsAction)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
    }

    // endregion

    // region Event Listeners

    @OnClick(R.id.login_button)
    fun login() = startupStore.dispatch(RoutingAction.NavigateToAuthenticationAction)

    @OnClick(R.id.login_register)
    fun register() = startupStore.dispatch(RoutingAction.NavigateToAuthenticationAction)

    // endregion
}
