package com.branhamplayer.android.auth.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.branhamplayer.android.auth.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse

@Navigator.Name("login")
class LoginNavigator(
    private val activity: FragmentActivity
) : Navigator<LoginNavigator.Destination>() {

    sealed class LoginOutcome {
        object Canceled : LoginOutcome()
        object DidNotReturnFromLoginScreen : LoginOutcome()
        object Success : LoginOutcome()

        data class Failed(
            val error: LoginError,
            val message: String?
        ) : LoginOutcome()
    }

    enum class LoginError {
        AnonymousUpgradeMergeConflict,
        DeveloperError,
        EmailLinkCrossDeviceLinkingError,
        EmailLinkDifferentAnonymousUserError,
        EmailLinkPromptForEmailError,
        EmailLinkWrongDeviceError,
        EmailMismatchError,
        InvalidEmailLinkError,
        NoNetwork,
        PlayServicesUpdateCanceled,
        ProviderError,
        UnknownError
    }

    companion object {
        private const val RequestCode = 45216

        fun loginOutcome(requestCode: Int, resultCode: Int, data: Intent?): LoginOutcome {
            if (requestCode != RequestCode) {
                return LoginOutcome.DidNotReturnFromLoginScreen
            }

            if (resultCode == Activity.RESULT_OK) {
                return LoginOutcome.Success
            }

            val response = IdpResponse.fromResultIntent(data) ?: return LoginOutcome.Canceled

            val errorCode = when (response.error?.errorCode) {
                ErrorCodes.ANONYMOUS_UPGRADE_MERGE_CONFLICT -> LoginError.AnonymousUpgradeMergeConflict
                ErrorCodes.DEVELOPER_ERROR -> LoginError.DeveloperError
                ErrorCodes.EMAIL_LINK_CROSS_DEVICE_LINKING_ERROR -> LoginError.EmailLinkCrossDeviceLinkingError
                ErrorCodes.EMAIL_LINK_DIFFERENT_ANONYMOUS_USER_ERROR -> LoginError.EmailLinkDifferentAnonymousUserError
                ErrorCodes.EMAIL_LINK_PROMPT_FOR_EMAIL_ERROR -> LoginError.EmailLinkPromptForEmailError
                ErrorCodes.EMAIL_LINK_WRONG_DEVICE_ERROR -> LoginError.EmailLinkWrongDeviceError
                ErrorCodes.EMAIL_MISMATCH_ERROR -> LoginError.EmailMismatchError
                ErrorCodes.INVALID_EMAIL_LINK_ERROR -> LoginError.InvalidEmailLinkError
                ErrorCodes.NO_NETWORK -> LoginError.NoNetwork
                ErrorCodes.PLAY_SERVICES_UPDATE_CANCELLED -> LoginError.PlayServicesUpdateCanceled
                ErrorCodes.PROVIDER_ERROR -> LoginError.ProviderError
                else -> LoginError.UnknownError
            }

            return LoginOutcome.Failed(
                error = errorCode,
                message = response.error?.message
            )
        }
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