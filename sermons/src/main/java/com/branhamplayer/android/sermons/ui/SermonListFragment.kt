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
import com.branhamplayer.android.sermons.actions.SermonListAction
import com.branhamplayer.android.sermons.adapters.SermonsAdapter
import com.branhamplayer.android.sermons.di.DaggerInjector
import com.branhamplayer.android.sermons.models.SermonModel
import com.branhamplayer.android.sermons.store.sermonsStore
import org.rekotlin.StoreSubscriber
import javax.inject.Inject

class SermonListFragment : Fragment(), StoreSubscriber<List<SermonModel>?> {

    private var unbinder: Unbinder? = null

    // region Components

    @JvmField
    @BindView(R.id.sermon_list)
    var sermonsRecyclerView: RecyclerView? = null

    // endregion

    // region DI

    @Inject
    lateinit var sermonAdapter: SermonsAdapter

    // endregion

    // region Controller

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.sermon_list_fragment, container, false)

        DaggerInjector.sermonsComponent?.inject(this)
        unbinder = ButterKnife.bind(this, view)

        sermonsStore.subscribe(this) {
            it.select { state ->
                state.sermons
            }.skipRepeats()
        }

        sermonsStore.dispatch(SermonListAction.ShowPhoneActionBarAction)

        sermonsRecyclerView?.adapter = sermonAdapter
        sermonsRecyclerView?.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
    }

    // endregion

    // region StoreSubscriber

    override fun newState(state: List<SermonModel>?) {
        state?.let {
            sermonAdapter.setSermons(it)
            sermonsRecyclerView?.adapter?.notifyDataSetChanged()
        }
    }

    // endregion
}
