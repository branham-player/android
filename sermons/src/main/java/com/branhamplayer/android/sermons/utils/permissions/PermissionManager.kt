package com.branhamplayer.android.sermons.utils.permissions

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.PERMISSION_DENIED
import androidx.core.content.PermissionChecker.PERMISSION_DENIED_APP_OP
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.branhamplayer.android.dagger.modules.RxJavaModule
import com.branhamplayer.android.data.database.BranhamPlayerDatabase
import com.branhamplayer.android.data.database.permissions.PermissionsEntity
import com.branhamplayer.android.utils.logging.Loggly
import com.branhamplayer.android.utils.logging.LogglyConstants.Tags.PERMISSIONS
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class PermissionManager @Inject constructor(
    private val activity: AppCompatActivity,
    private val database: BranhamPlayerDatabase,
    private val dexter: DexterBuilder.Permission,
    @Named(RxJavaModule.BG) private val bg: Scheduler,
    @Named(RxJavaModule.UI) private val ui: Scheduler
) {

    fun checkPermissionStatus(permission: String) = checkAndroidForPermissionStatus(permission)
        .flatMap { status ->
            if (status == PermissionStatus.Granted) {
                return@flatMap Single.just(status)
            }

            database
                .permissionsDao()
                .isDeniedPermanently(permission)
                .flatMapSingle<PermissionStatus> {
                    // Condition: found that the permission is denied permanently
                    // Runs only when there is a value, and throws if the Maybe is empty
                    // causing the onErrorReturn to run
                    Single.just(PermissionStatus.DeniedPermanently)
                }
                .onErrorReturn {
                    // Condition: permission was not denied permanently, only once
                    // Runs when a value is not found or the database cannot be accessed
                    status
                }
        }
        .flatMap { status ->
            updatePermissionStatusInDatabase(permission, status)
        }
        .subscribeOn(bg)
        .observeOn(ui)

    fun requestPermission(permission: String): Single<PermissionStatus> = checkPermissionStatus(permission)
        .flatMap {
            when (it) {
                PermissionStatus.Granted -> {
                    Loggly.i(PERMISSIONS, "Permission '$permission' previously granted")
                    return@flatMap Single.just(PermissionStatus.Granted)
                }

                PermissionStatus.DeniedPermanently -> {
                    Loggly.i(PERMISSIONS, "Permission '$permission' denied permanently")
                    return@flatMap Single.just(PermissionStatus.DeniedPermanently)
                }

                else -> Loggly.i(PERMISSIONS, "Permission '$permission' denied once, requesting it again")
            }

            showPermissionDialogPrompt(permission)
        }
        .observeOn(bg)
        .flatMap { status ->
            updatePermissionStatusInDatabase(permission, status)
        }
        .observeOn(ui)

    private fun checkAndroidForPermissionStatus(permission: String): Single<PermissionStatus> {
        val status = when {
            PermissionChecker.checkSelfPermission(activity, permission) == PERMISSION_DENIED -> PermissionStatus.DeniedOnce
            PermissionChecker.checkSelfPermission(activity, permission) == PERMISSION_DENIED_APP_OP -> PermissionStatus.DeniedPermanently
            PermissionChecker.checkSelfPermission(activity, permission) == PERMISSION_GRANTED -> PermissionStatus.Granted
            else -> PermissionStatus.DeniedOnce
        }

        return Single.just(status)
    }

    private fun clearPermissionDeniedStatus(permission: String, status: PermissionStatus) = database
        .permissionsDao()
        .clearDeniedPermanently(permission)
        .andThen(
            Single.just(status)
        )

    private fun markPermissionDeniedStatus(permission: String, status: PermissionStatus) = database
        .permissionsDao()
        .markAsDeniedPermanently(PermissionsEntity(
            permission = permission
        ))
        .andThen(
            Single.just(status)
        )

    private fun showPermissionDialogPrompt(permission: String) = Single.create<PermissionStatus> { subscriber ->
        dexter
            .withPermission(permission)
            .withListener(object : PermissionListener {

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    if (response?.isPermanentlyDenied == true) {
                        Loggly.w(PERMISSIONS, "Just asked for permission, '$permission' permanently denied")
                        subscriber.onSuccess(PermissionStatus.DeniedPermanently)
                    } else {
                        Loggly.w(PERMISSIONS, "Just asked for permission, '$permission' denied once")
                        subscriber.onSuccess(PermissionStatus.DeniedOnce)
                    }
                }

                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    Loggly.i(PERMISSIONS, "Just asked for permission, '$permission' granted")
                    subscriber.onSuccess(PermissionStatus.Granted)
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    Loggly.i(PERMISSIONS, "Permission rationale triggered for '$permission'")
                    token?.continuePermissionRequest()
                }
            })
            .check()
    }

    private fun updatePermissionStatusInDatabase(permission: String, status: PermissionStatus) =
        if (status == PermissionStatus.DeniedPermanently) {
            markPermissionDeniedStatus(permission, status)
        } else {
            clearPermissionDeniedStatus(permission, status)
        }

    sealed class PermissionStatus {
        object DeniedOnce : PermissionStatus()
        object DeniedPermanently : PermissionStatus()
        object Granted : PermissionStatus()
    }
}
