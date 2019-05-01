package com.branhamplayer.android.sermons.actions

import org.rekotlin.Action

sealed class DataAction : Action {
    object FetchSermonListAction : DataAction()
}
