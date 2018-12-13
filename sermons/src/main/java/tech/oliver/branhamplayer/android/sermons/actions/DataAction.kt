package tech.oliver.branhamplayer.android.sermons.actions

import org.rekotlin.Action
import tech.oliver.branhamplayer.android.sermons.models.SermonModel

sealed class DataAction: Action {
    class FetchSermonListAction: DataAction()
}
