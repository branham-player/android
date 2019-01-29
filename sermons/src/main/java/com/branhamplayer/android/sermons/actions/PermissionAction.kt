package com.branhamplayer.android.sermons.actions

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import org.rekotlin.Action

sealed class PermissionAction : Action {
    data class GetFileReadPermissionAction(val activity: AppCompatActivity) : PermissionAction()
    data class ShowPermissionDeniedErrorAction(val context: Context) : PermissionAction()
}