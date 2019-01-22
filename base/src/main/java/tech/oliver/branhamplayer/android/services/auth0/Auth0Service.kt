package tech.oliver.branhamplayer.android.services.auth0

import android.content.Context
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.callback.BaseCallback
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import io.reactivex.Single
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import org.koin.standalone.inject

class Auth0Service : KoinComponent {

    private val authClient: AuthenticationAPIClient by inject()

    fun getUserProfileInformation(context: Context): Single<UserProfile> {

        val credentialsManager: CredentialsManager = get { parametersOf(context) }

        return Single.create<Credentials> { subscriber ->
            credentialsManager.getCredentials(object : BaseCallback<Credentials, CredentialsManagerException> {
                override fun onFailure(error: CredentialsManagerException?) {
                    subscriber.onError(Throwable(error))
                }

                override fun onSuccess(payload: Credentials?) {
                    subscriber.onSuccess(payload!!)
                }
            })
        }.flatMap { credentials ->
            Single.create<UserProfile> { subscriber ->
                val accessToken = credentials.accessToken

                if (accessToken == null) {
                    subscriber.onError(Throwable("Access token is null"))
                    return@create
                }

                authClient.userInfo(accessToken).start(object : BaseCallback<UserProfile, AuthenticationException> {
                    override fun onFailure(error: AuthenticationException?) {
                        subscriber.onError(Throwable(error))
                    }

                    override fun onSuccess(payload: UserProfile?) {
                        if (payload == null) {
                            subscriber.onError(Throwable("Could not get the user's profile"))
                            return
                        }

                        subscriber.onSuccess(payload)
                    }
                })
            }
        }
    }
}