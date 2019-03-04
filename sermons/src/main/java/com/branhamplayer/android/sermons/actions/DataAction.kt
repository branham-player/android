package com.branhamplayer.android.sermons.actions

import androidx.annotation.StringRes
import org.rekotlin.Action

sealed class DataAction : Action {
    object FetchSermonListAction : DataAction()
    data class SetTitleAction(@StringRes val title: Int) : DataAction()
}
