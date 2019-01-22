package tech.oliver.branhamplayer.android.sermons.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluelinelabs.conductor.RestoreViewOnCreateController
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import org.koin.standalone.inject
import org.rekotlin.StoreSubscriber
import tech.oliver.branhamplayer.android.sermons.R
import tech.oliver.branhamplayer.android.sermons.actions.DataAction
import tech.oliver.branhamplayer.android.sermons.actions.DrawerAction
import tech.oliver.branhamplayer.android.sermons.actions.PermissionAction
import tech.oliver.branhamplayer.android.sermons.actions.ProfileAction
import tech.oliver.branhamplayer.android.sermons.adapters.SermonsAdapter
import tech.oliver.branhamplayer.android.sermons.shared.sermonsStore
import tech.oliver.branhamplayer.android.sermons.states.SermonsState

class SermonsController : RestoreViewOnCreateController(), KoinComponent, StoreSubscriber<SermonsState> {

    private val sermonAdapter: SermonsAdapter by inject { parametersOf(applicationContext) }
    private var sermonsRecyclerView: RecyclerView? = null

    // region Controller

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        val view = inflater.inflate(R.layout.sermons_controller, container, false)
        val linearLayoutManager: LinearLayoutManager = get { parametersOf(applicationContext) }

        sermonsStore.subscribe(this)

        sermonsRecyclerView = view?.findViewById(R.id.sermon_list)
        sermonsRecyclerView?.adapter = sermonAdapter
        sermonsRecyclerView?.layoutManager = linearLayoutManager

        activity?.let {
            val compatActivity = it as AppCompatActivity
            val isTablet = resources?.getBoolean(R.bool.is_tablet) == true

            sermonsStore.dispatch(DataAction.SetTitleAction(it.applicationContext, R.string.navigation_sermons))
            sermonsStore.dispatch(PermissionAction.GetFileReadPermissionAction(compatActivity))

            if (isTablet) {
                val toolbar: Toolbar = it.findViewById(R.id.primary_toolbar)

                sermonsStore.dispatch(DrawerAction.CreateDrawerWithoutProfileAction(compatActivity, toolbar, savedViewState, 0))
                sermonsStore.dispatch(ProfileAction.GetUserProfileAction(it.applicationContext))
            }
        }

        return view
    }

    // endregion

    // region StoreSubscriber

    override fun newState(state: SermonsState) {
        val toolbar: Toolbar? = activity?.findViewById(R.id.sermon_list_toolbar)

        state.sermonList?.let {
            sermonAdapter.setSermons(it)
            sermonsRecyclerView?.adapter?.notifyDataSetChanged()
        }

        state.title?.let {
            toolbar?.title = it
        }
    }

    // endregion
}
