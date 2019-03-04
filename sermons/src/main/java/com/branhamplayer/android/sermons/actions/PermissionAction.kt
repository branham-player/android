package com.branhamplayer.android.sermons.actions

import org.rekotlin.Action

sealed class PermissionAction : Action {
    object GetFileReadPermissionAction : PermissionAction()
    object ShowPermissionDeniedErrorAction : PermissionAction()
}