package com.branhamplayer.android.sermons.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewFlipper
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.branhamplayer.android.sermons.R
import com.branhamplayer.android.sermons.actions.RoutingAction
import com.branhamplayer.android.sermons.actions.SermonListAction
import com.branhamplayer.android.sermons.dagger.DaggerInjector
import com.branhamplayer.android.sermons.states.SermonsState
import com.branhamplayer.android.sermons.store.sermonsStore
import com.branhamplayer.android.sermons.ui.adapters.SermonListAdapter
import com.branhamplayer.android.sermons.utils.permissions.PermissionManager
import org.rekotlin.StoreSubscriber
import javax.inject.Inject

class SermonListFragment : Fragment(), StoreSubscriber<SermonsState> {

    private var unbinder: Unbinder? = null

    // region Components

    @JvmField
    @BindView(R.id.sermon_list)
    var sermonsRecyclerView: RecyclerView? = null

    @JvmField
    @BindView(R.id.sermon_list_view_flipper)
    var viewFlipper: ViewFlipper? = null

    // endregion

    // region Dagger

    @Inject
    lateinit var sermonAdapter: SermonListAdapter

    // endregion

    // region Controller

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.sermon_list_fragment, container, false)

        DaggerInjector.sermonsComponent?.inject(this)

        unbinder = ButterKnife.bind(this, view)
        sermonsStore.subscribe(this)

        sermonsRecyclerView?.adapter = sermonAdapter
        sermonsRecyclerView?.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sermonsStore.dispatch(SermonListAction.CheckFileReadPermissionAction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
    }

    // endregion

    // region StoreSubscriber

    override fun newState(state: SermonsState) {
        state.sermonList?.let {
            sermonAdapter.setSermons(it)
            sermonsRecyclerView?.adapter?.notifyDataSetChanged()
        }

        viewFlipper?.displayedChild = when {
            state.fileReadPermission == PermissionManager.PermissionStatus.DeniedPermanently -> 1
            state.fileReadPermission == PermissionManager.PermissionStatus.Granted -> 2
            state.fileReadPermission != PermissionManager.PermissionStatus.Granted -> 0
            else -> 0
        }
    }

    // endregion

    @OnClick(R.id.denied_permanently_button)
    fun goToAppSettings() =
        sermonsStore.dispatch(RoutingAction.NavigateToApplicationSettings)

    @OnClick(R.id.request_permission_button)
    fun requestPermission() =
        sermonsStore.dispatch(SermonListAction.RequestFileReadPermissionAction)
}
