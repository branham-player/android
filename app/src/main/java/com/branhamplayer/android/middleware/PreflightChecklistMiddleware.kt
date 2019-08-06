package com.branhamplayer.android.middleware

import android.annotation.SuppressLint
import com.branhamplayer.android.BuildConfig
import com.branhamplayer.android.StartupConstants
import com.branhamplayer.android.actions.PreflightChecklistAction
import com.branhamplayer.android.actions.RoutingAction
import com.branhamplayer.android.base.redux.TypedMiddleware
import com.branhamplayer.android.dagger.modules.RxJavaModule
import com.branhamplayer.android.data.DataConstants
import com.branhamplayer.android.data.database.BranhamPlayerDatabase
import com.branhamplayer.android.data.database.versions.VersionsEntity
import com.branhamplayer.android.data.mappers.MetadataMapper
import com.branhamplayer.android.data.network.RawMetadataNetworkProvider
import com.branhamplayer.android.states.StartupState
import com.branhamplayer.android.utils.Semver
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.reactivex.Scheduler
import org.rekotlin.DispatchFunction
import javax.inject.Inject
import javax.inject.Named

class PreflightChecklistMiddleware @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    private val branhamPlayerDatabase: BranhamPlayerDatabase,
    private val rawMetadataNetworkProvider: RawMetadataNetworkProvider,
    private val metadataMapper: MetadataMapper,
    @Named(RxJavaModule.BG) private val bg: Scheduler,
    @Named(RxJavaModule.UI) private val ui: Scheduler
) : TypedMiddleware<PreflightChecklistAction, StartupState> {

    override fun invoke(dispatch: DispatchFunction, action: PreflightChecklistAction, oldState: StartupState?) {
        when (action) {
            is PreflightChecklistAction.CheckMessageAction -> checkMessage(dispatch)
            is PreflightChecklistAction.CheckMetadataAction -> checkMetadataAction(dispatch)
            is PreflightChecklistAction.CheckMinimumVersionAction -> checkMinimumVersion(dispatch)
            is PreflightChecklistAction.CheckPlatformStatusAction -> checkPlatformStatus(dispatch)
        }
    }

    private fun checkMessage(dispatch: DispatchFunction) {
        val message = firebaseRemoteConfig.getString(StartupConstants.PreflightChecklist.message)

        if (message.isNullOrBlank()) {
            dispatch(RoutingAction.NavigateToWelcomeAction)
        } else {
            dispatch(PreflightChecklistAction.NotifyWithMessageAction(message))
        }
    }

    @SuppressLint("CheckResult")
    private fun checkMetadataAction(dispatch: DispatchFunction) {
        val configuredVersion =
            Semver(firebaseRemoteConfig.getString(StartupConstants.PreflightChecklist.metadataVersion))

        branhamPlayerDatabase
            .metadataDao()
            .fetchFirstIfExists()
            .flatMap { branhamPlayerDatabase.versionsDao().fetchMetadataVersion() }
            .subscribeOn(bg)
            .observeOn(ui)
            .subscribe({ versionInformation ->
                // Data returned
                val downloadedVersion = Semver(versionInformation.version)

                if (configuredVersion > downloadedVersion) {
                    updateLocalMetadata(configuredVersion.toString(), dispatch)
                } else {
                    dispatch(PreflightChecklistAction.CheckMessageAction)
                }
            }, {
                // Error encountered
                updateLocalMetadata(configuredVersion.toString(), dispatch)
            }, {
                // No data returned
                updateLocalMetadata(configuredVersion.toString(), dispatch)
            })
    }

    private fun checkMinimumVersion(dispatch: DispatchFunction) {
        val appVersion = Semver(BuildConfig.VERSION_NAME)
        val minimumVersion = Semver(firebaseRemoteConfig.getString(StartupConstants.PreflightChecklist.minimumVersion))

        if (appVersion >= minimumVersion) {
            dispatch(PreflightChecklistAction.CheckMetadataAction)
        } else {
            dispatch(PreflightChecklistAction.StopAppWithMinimumVersionFailureAction)
        }
    }

    private fun checkPlatformStatus(dispatch: DispatchFunction) =
        if (firebaseRemoteConfig.getBoolean(StartupConstants.PreflightChecklist.platformStatus)) {
            dispatch(PreflightChecklistAction.CheckMinimumVersionAction)
        } else {
            val message = firebaseRemoteConfig.getString(StartupConstants.PreflightChecklist.message)
            dispatch(PreflightChecklistAction.StopAppWithPlatformDownAction(message))
        }

    @SuppressLint("CheckResult")
    private fun updateLocalMetadata(configuredVersion: String, dispatch: DispatchFunction) {
        branhamPlayerDatabase
            .metadataDao()
            .deleteAll()
            .andThen(rawMetadataNetworkProvider.getRawMetadata(configuredVersion))
            .map { rawMetadata ->
                metadataMapper.map(rawMetadata)
            }
            .flatMapCompletable { mappedMetadata ->
                branhamPlayerDatabase.metadataDao().insertAll(mappedMetadata)
            }
            .andThen(
                branhamPlayerDatabase.versionsDao().insertOrUpdate(
                    VersionsEntity(0, DataConstants.Database.Tables.Metadata.metadataVersion, configuredVersion)
                )
            )
            .subscribeOn(bg)
            .observeOn(ui)
            .subscribe({
                dispatch(PreflightChecklistAction.CheckMessageAction)
            }, {
                dispatch(PreflightChecklistAction.StopAppWithMetadataFailureAction)
            })
    }
}
