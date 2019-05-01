package com.branhamplayer.android.sermons.reducers

import android.content.Context
import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.sermons.actions.DataAction
import com.branhamplayer.android.sermons.mappers.SermonMapper
import com.branhamplayer.android.sermons.repositories.SermonsRepository
import com.branhamplayer.android.sermons.states.SermonsState
import javax.inject.Inject

class DataReducer @Inject constructor(
    private val context: Context,
    private val mapper: SermonMapper,
    private val repository: SermonsRepository
) : TypedReducer<DataAction, SermonsState> {

    override fun invoke(action: DataAction, oldState: SermonsState) = when (action) {
        is DataAction.FetchSermonListAction -> fetchSermonList(oldState)
    }

    private fun fetchSermonList(state: SermonsState): SermonsState {
        val filesOnDisk = repository.getSermons().value
        val sermons = mapper.map(filesOnDisk)

        return state.copy(
            sermonList = sermons?.value
        )
    }
}
