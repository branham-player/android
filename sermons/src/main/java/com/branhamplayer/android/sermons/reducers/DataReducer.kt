package com.branhamplayer.android.sermons.reducers

import android.content.Context
import androidx.annotation.StringRes
import com.branhamplayer.android.sermons.actions.DataAction
import com.branhamplayer.android.sermons.mappers.SermonMapper
import com.branhamplayer.android.sermons.repositories.SermonsRepository
import com.branhamplayer.android.sermons.states.SermonsState
import org.koin.standalone.StandAloneContext

class DataReducer {
    companion object {

        fun reduce(action: DataAction, oldState: SermonsState) = when (action) {
            is DataAction.FetchSermonListAction -> fetchSermonList(oldState)
            is DataAction.SetTitleAction -> setTitle(oldState, action.context, action.title)
        }

        private fun fetchSermonList(state: SermonsState): SermonsState {
            val repo: SermonsRepository = StandAloneContext.getKoin().koinContext.get()
            val mapper: SermonMapper = StandAloneContext.getKoin().koinContext.get()

            val filesOnDisk = repo.getSermons().value
            val sermons = mapper.map(filesOnDisk)

            return state.copy(
                    sermonList = sermons?.value
            )
        }

        private fun setTitle(state: SermonsState, context: Context, @StringRes title: Int) = state.copy(
                title = context.getString(title)
        )
    }
}
