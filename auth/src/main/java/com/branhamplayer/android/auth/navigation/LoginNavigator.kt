package com.branhamplayer.android.auth.navigation

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.branhamplayer.android.auth.R
import com.firebase.ui.auth.AuthUI

@Navigator.Name("login")
class LoginNavigator(
    private val activity: FragmentActivity
) : Navigator<LoginNavigator.Destination>() {

    companion object {
        const val RequestCode = 45216
    }

    override fun createDestination() = Destination(this)

    override fun navigate(destination: Destination, args: Bundle?, navOptions: NavOptions?, navigatorExtras: Extras?): NavDestination? {
        val allowedProviders = mutableListOf<AuthUI.IdpConfig>()

        if (destination.allowEmail) allowedProviders.add(AuthUI.IdpConfig.EmailBuilder().build())
        if (destination.allowGoogle) allowedProviders.add(AuthUI.IdpConfig.GoogleBuilder().build())

        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(allowedProviders)
            .build()

        activity.startActivityForResult(intent, RequestCode)
        return null // Managed by the new activity
    }

    override fun popBackStack() = true // Managed by the new activity

    @NavDestination.ClassType(FragmentActivity::class)
    class Destination(navigator: Navigator<out NavDestination>) : NavDestination(navigator) {

        var allowEmail = true
        var allowGoogle = true

        override fun onInflate(context: Context, attrs: AttributeSet) {
            super.onInflate(context, attrs)

            context.withStyledAttributes(attrs, R.styleable.LoginNavigator, 0, 0) {
                allowEmail = getBoolean(R.styleable.LoginNavigator_allowEmail, true)
                allowGoogle = getBoolean(R.styleable.LoginNavigator_allowGoogle, true)
            }
        }
    }
}