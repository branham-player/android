package com.branhamplayer.android.sermons.reducers

import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.sermons.actions.SermonListAction
import com.branhamplayer.android.sermons.mappers.SermonListMapper
import com.branhamplayer.android.sermons.repositories.SermonListRepository
import com.branhamplayer.android.sermons.states.SermonsState
import javax.inject.Inject

class SermonListReducer @Inject constructor(
    private val sermonListMapper: SermonListMapper,
    private val repository: SermonListRepository
) : TypedReducer<SermonListAction, SermonsState> {

    override fun invoke(action: SermonListAction, oldState: SermonsState) = when (action) {
        is SermonListAction.FetchListAction -> fetchList(oldState)
        else -> oldState
    }

    private fun fetchList(state: SermonsState): SermonsState {
        val filesOnDisk = repository.getSermons().value
        val sermons = sermonListMapper.map(filesOnDisk)

        return state.copy(
            sermonList = sermons?.value
        )
    }
}
