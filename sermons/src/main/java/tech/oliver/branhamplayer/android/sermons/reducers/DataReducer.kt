package tech.oliver.branhamplayer.android.sermons.reducers

import org.koin.standalone.StandAloneContext
import tech.oliver.branhamplayer.android.sermons.actions.DataAction
import tech.oliver.branhamplayer.android.sermons.mappers.SermonMapper
import tech.oliver.branhamplayer.android.sermons.repositories.SermonsRepository
import tech.oliver.branhamplayer.android.sermons.states.SermonsState

class DataReducer {
    companion object {

        fun reduce(action: DataAction, sermonsState: SermonsState) = when (action) {
            is DataAction.FetchSermonListAction -> fetchSermonList(sermonsState)
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
    }
}
