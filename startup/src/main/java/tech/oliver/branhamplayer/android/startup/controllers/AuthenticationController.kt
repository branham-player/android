package tech.oliver.branhamplayer.android.startup.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.auth0.android.authentication.storage.CredentialsManager
import com.bluelinelabs.conductor.Controller
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import tech.oliver.branhamplayer.android.startup.R
import tech.oliver.branhamplayer.android.startup.actions.AuthenticationAction
import tech.oliver.branhamplayer.android.startup.actions.RoutingAction
import tech.oliver.branhamplayer.android.startup.shared.startupStore

class AuthenticationController : Controller(), KoinComponent {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.authentication_controller, container, false)
        val loginButton: AppCompatButton? = view.findViewById(R.id.login_button)
        val registerLink: AppCompatTextView? = view.findViewById(R.id.login_register)

        loginButton?.setOnClickListener {
            loginOrRegister()
        }

        registerLink?.setOnClickListener {
            loginOrRegister()
        }

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)

        applicationContext?.let {
            val userCredentials: CredentialsManager = get { parametersOf(it) }

            if (userCredentials.hasValidCredentials()) {
                startupStore.dispatch(RoutingAction.NavigateToSermonsAction(it))
            }
        }
    }

    private fun loginOrRegister() {
        activity?.let {
            startupStore.dispatch(AuthenticationAction.DoLoginAction(it))
        }
    }
}
