package tech.oliver.branhamplayer.android.sermons.states

import org.rekotlin.StateType
import tech.oliver.branhamplayer.android.sermons.models.SermonModel

data class SermonsState(
        val sermonList: List<SermonModel>? = null
) : StateType
