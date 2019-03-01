package com.branhamplayer.android.sermons.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.branhamplayer.android.R as RBase
import com.branhamplayer.android.sermons.R
import com.branhamplayer.android.sermons.adapters.SermonsAdapter
import com.branhamplayer.android.sermons.di.DaggerInjector
import com.branhamplayer.android.sermons.store.sermonsStore
import com.branhamplayer.android.sermons.states.SermonsState
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import org.koin.standalone.inject
import org.rekotlin.StoreSubscriber

class SermonListFragment : Fragment(), KoinComponent, StoreSubscriber<SermonsState> {

    private val sermonAdapter: SermonsAdapter by inject { parametersOf(context) }
    private var unbinder: Unbinder? = null

    @JvmField
    @BindView(R.id.sermon_list)
    var sermonsRecyclerView: RecyclerView? = null

    // region Controller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerInjector.buildPermissionComponent()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.sermon_list_fragment, container, false)
        val linearLayoutManager: LinearLayoutManager = get { parametersOf(context) }

        unbinder = ButterKnife.bind(this, view)
        sermonsStore.subscribe(this)

        sermonsRecyclerView?.adapter = sermonAdapter
        sermonsRecyclerView?.layoutManager = linearLayoutManager

        return view
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
    }

    // endregion
}
