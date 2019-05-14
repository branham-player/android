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
import com.branhamplayer.android.sermons.R
import com.branhamplayer.android.sermons.dagger.DaggerInjector
import com.branhamplayer.android.sermons.states.SermonsState
import com.branhamplayer.android.sermons.store.sermonsStore
import com.branhamplayer.android.sermons.ui.adapters.SermonListAdapter
import org.rekotlin.StoreSubscriber
import javax.inject.Inject
import com.branhamplayer.android.R as RBase

class SermonListFragment : Fragment(), StoreSubscriber<SermonsState> {

    private var unbinder: Unbinder? = null

    // region Components

    @BindView(R.id.sermon_list)
    lateinit var sermonsRecyclerView: RecyclerView

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

        sermonsRecyclerView.adapter = sermonAdapter
        sermonsRecyclerView.layoutManager = LinearLayoutManager(context)

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
            sermonsRecyclerView.adapter?.notifyDataSetChanged()
        }
    }

    // endregion
}
