package com.branhamplayer.android.sermons.utils.permissions

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.branhamplayer.android.utils.logging.Loggly
import com.branhamplayer.android.utils.logging.LogglyConstants.Tags.PERMISSIONS
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.reactivex.Single
import javax.inject.Inject

class PermissionManager @Inject constructor(
    private val context: Context,
    private val dexter: DexterBuilder.Permission
) {

    fun getSinglePermission(permission: String): Single<PermissionStatus> = Single.create { subscriber ->

        if (hasPermission(permission)) {
            Loggly.i(PERMISSIONS, "Permission '$permission' previously granted")
            subscriber.onSuccess(PermissionStatus.Granted)
            return@create
        }

        dexter
            .withPermission(permission)
            .withListener(object : PermissionListener {

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    if (response?.isPermanentlyDenied == true) {
                        Loggly.w(PERMISSIONS, "Permission '$permission' permanently denied")
                        subscriber.onSuccess(PermissionStatus.DeniedPermanently)
                    } else {
                        Loggly.w(PERMISSIONS, "Permission '$permission' denied once")
                        subscriber.onSuccess(PermissionStatus.DeniedOnce)
                    }
                }

                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    Loggly.i(PERMISSIONS, "Permission '$permission' granted")
                    subscriber.onSuccess(PermissionStatus.Granted)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    Loggly.i(PERMISSIONS, "Permission rationale triggered for '$permission'")
                    token?.continuePermissionRequest()
                }
            })
            .check()
    }

    fun hasPermission(permission: String) = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    sealed class PermissionStatus {
        object DeniedOnce : PermissionStatus()
        object DeniedPermanently : PermissionStatus()
        object Granted : PermissionStatus()
    }
}
