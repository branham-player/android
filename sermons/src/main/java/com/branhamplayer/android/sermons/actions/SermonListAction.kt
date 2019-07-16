package com.branhamplayer.android.sermons.actions

import com.branhamplayer.android.base.redux.BaseAction

sealed class SermonListAction : BaseAction {
    object FetchListAction : SermonListAction()

    object CheckFileReadPermissionAction : SermonListAction()
    object RequestFileReadPermissionAction : SermonListAction()
    object ShowPermissionDeniedOnceErrorAction : SermonListAction()
    object ShowPermissionDeniedPermanentlyErrorAction : SermonListAction()
    object ShowPermissionGrantedAction : SermonListAction()
}
