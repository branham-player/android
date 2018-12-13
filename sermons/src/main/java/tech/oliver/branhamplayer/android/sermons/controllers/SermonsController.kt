package tech.oliver.branhamplayer.android.sermons.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluelinelabs.conductor.Controller
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import org.koin.standalone.inject
import org.rekotlin.StoreSubscriber
import tech.oliver.branhamplayer.android.sermons.R
import tech.oliver.branhamplayer.android.sermons.actions.PermissionAction
import tech.oliver.branhamplayer.android.sermons.adapters.SermonsAdapter
import tech.oliver.branhamplayer.android.sermons.shared.sermonsStore
import tech.oliver.branhamplayer.android.sermons.states.SermonsState

class SermonsController : Controller(), KoinComponent, StoreSubscriber<SermonsState> {

    private val sermonAdapter: SermonsAdapter by inject { parametersOf(applicationContext) }
    private var sermonsRecyclerView: RecyclerView? = null

    // region Controller

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.sermons_controller, container, false)
        val linearLayoutManager: LinearLayoutManager = get { parametersOf(applicationContext) }

        sermonsStore.subscribe(this)

        sermonsRecyclerView = view?.findViewById(R.id.sermon_list)
        sermonsRecyclerView?.adapter = sermonAdapter
        sermonsRecyclerView?.layoutManager = linearLayoutManager

        activity?.let {
            sermonsStore.dispatch(PermissionAction.GetFileReadPermissionAction(it))
        }

        return view
    }

    // endregion

    // region StoreSubscriber

    override fun newState(state: SermonsState) {
        state.sermonList?.let {
            sermonAdapter.setSermons(it)
            sermonsRecyclerView?.adapter?.notifyDataSetChanged()
        }
    }

    // endregion
}
