package com.branhamplayer.android.sermons.reducers

import com.branhamplayer.android.base.redux.TypedReducer
import com.branhamplayer.android.sermons.actions.SermonListAction
import com.branhamplayer.android.sermons.mappers.SermonListMapper
import com.branhamplayer.android.sermons.repositories.SermonListRepository
import com.branhamplayer.android.sermons.states.SermonsState
import com.branhamplayer.android.sermons.utils.permissions.PermissionManager
import javax.inject.Inject

class SermonListReducer @Inject constructor(
    private val sermonListMapper: SermonListMapper,
    private val repository: SermonListRepository
) : TypedReducer<SermonListAction, SermonsState> {

    override fun invoke(action: SermonListAction, oldState: SermonsState) = when (action) {
        is SermonListAction.FetchListAction -> fetchList(oldState)
        is SermonListAction.ShowPermissionDeniedOnceErrorAction -> showPermissionDeniedOnceError(oldState)
        is SermonListAction.ShowPermissionDeniedPermanentlyErrorAction -> showPermissionDeniedPermanentlyError(oldState)
        is SermonListAction.ShowPermissionGrantedAction -> showPermissionGranted(oldState)
        else -> oldState
    }

    private fun fetchList(state: SermonsState): SermonsState {
        val filesOnDisk = repository.getSermons().value
        val sermons = sermonListMapper.map(filesOnDisk)

        return state.copy(
            sermonList = sermons?.value
        )
    }

    private fun showPermissionDeniedOnceError(oldState: SermonsState) = oldState.copy(
        fileReadPermission = PermissionManager.PermissionStatus.DeniedOnce
    )

    private fun showPermissionDeniedPermanentlyError(oldState: SermonsState) = oldState.copy(
        fileReadPermission = PermissionManager.PermissionStatus.DeniedPermanently
    )

    private fun showPermissionGranted(oldState: SermonsState) = oldState.copy(
        fileReadPermission = PermissionManager.PermissionStatus.Granted
    )
}
