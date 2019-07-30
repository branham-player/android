package com.branhamplayer.android.auth

import android.app.Activity
import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.tedpark.tedonactivityresult.rx2.TedRxOnActivityResult
import io.reactivex.Single
import javax.inject.Inject

class LoginUtility @Inject constructor(
    private val context: Context
) {

    sealed class Outcome {
        object Canceled : Outcome()
        object Success : Outcome()

        data class Error(
            val errorCode: Int?
        ) : Outcome()
    }

    fun login(): Single<Outcome> {
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(
                mutableListOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build()
                )
            )
            .build()

        return TedRxOnActivityResult.with(context)
            .startActivityForResult(intent)
            .flatMap { result ->
                val response = IdpResponse.fromResultIntent(result.data)

                val outcome = when {
                    result.resultCode == Activity.RESULT_OK -> Outcome.Success
                    response == null -> Outcome.Canceled // User pressed back button
                    else -> Outcome.Error(response.error?.errorCode)
                }

                Single.just(outcome)
            }
    }
}