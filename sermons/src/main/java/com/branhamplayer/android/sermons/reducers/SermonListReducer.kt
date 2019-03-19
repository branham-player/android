package com.branhamplayer.android.sermons.reducers

import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.sermons.actions.SermonListAction
import com.branhamplayer.android.sermons.mappers.SermonMapper
import com.branhamplayer.android.sermons.repositories.SermonsRepository
import com.branhamplayer.android.sermons.states.SermonsState
import javax.inject.Inject

class SermonListReducer @Inject constructor(
    private val mapper: SermonMapper,
    private val repository: SermonsRepository
) : TypedReducer<SermonListAction, SermonsState> {

    // region TypedReducer

    override fun invoke(action: SermonListAction, oldState: SermonsState) = when (action) {
        is SermonListAction.FetchSermonListAction -> fetchSermonList(oldState)
        is SermonListAction.SetTitleAction -> setTitle(oldState, action)
        is SermonListAction.ShowPhoneActionBarAction -> showPhoneActionBar(oldState)
        else -> oldState
    }

    // endregion

    private fun fetchSermonList(state: SermonsState): SermonsState {
        val filesOnDisk = repository.getSermons().value
        val sermons = mapper.map(filesOnDisk)

        return state.copy(
            sermons = sermons?.value
        )
    }

    private fun setTitle(state: SermonsState, action: SermonListAction.SetTitleAction) = state.copy(
        title = action.title
    )

    private fun showPhoneActionBar(oldState: SermonsState) = oldState.copy(
        phoneActionBarVisible = true
    )
}
