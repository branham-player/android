package tech.oliver.branhamplayer.android.sermons.actions

import android.content.Context
import androidx.annotation.StringRes
import org.rekotlin.Action

sealed class DataAction : Action {
    class FetchSermonListAction : DataAction()
    data class SetTitleAction(val context: Context, @StringRes val title: Int) : DataAction()
}
