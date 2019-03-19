package com.branhamplayer.android.sermons.actions

sealed class SermonListAction : SermonsAction {
    object FetchSermonListAction : SermonListAction()
    object GetFileReadPermissionAction : SermonListAction()
    data class SetTitleAction(val title: String) : SermonListAction()
    object ShowPhoneActionBarAction : SermonListAction()
    object ShowPermissionDeniedErrorAction : SermonListAction()
}
