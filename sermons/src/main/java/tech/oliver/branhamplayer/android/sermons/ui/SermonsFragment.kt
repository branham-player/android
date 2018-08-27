package tech.oliver.branhamplayer.android.sermons.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tech.oliver.branhamplayer.android.sermons.R
import tech.oliver.branhamplayer.android.sermons.adapters.SermonsAdapter
import tech.oliver.branhamplayer.android.sermons.viewmodels.SermonsViewModel

class SermonsFragment : Fragment() {

    private var sermonAdapter: SermonsAdapter? = null
    private var sermonsRecyclerView: RecyclerView? = null
    private lateinit var viewModel: SermonsViewModel

    // region Fragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(SermonsViewModel::class.java)
        subscribeUi()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.sermons_fragment, container, false)

        sermonAdapter = SermonsAdapter(context)
        sermonsRecyclerView = view?.findViewById(R.id.sermon_list)
        sermonsRecyclerView?.adapter = sermonAdapter
        sermonsRecyclerView?.layoutManager = LinearLayoutManager(context)

        return view
    }

    // endregion

    private fun subscribeUi() {
        viewModel.sermons.observe(viewLifecycleOwner, Observer {
            sermonAdapter?.setSermons(it)
        })
    }

    // region Static Constructor

    companion object {
        fun newInstance() = SermonsFragment()
    }

    // endregion
}
