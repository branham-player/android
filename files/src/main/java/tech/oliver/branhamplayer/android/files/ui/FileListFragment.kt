package tech.oliver.branhamplayer.android.files.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tech.oliver.branhamplayer.android.files.R
import tech.oliver.branhamplayer.android.files.adapters.FileListAdapter
import tech.oliver.branhamplayer.android.files.viewmodels.FileListViewModel

class FileListFragment : Fragment() {

    private var fileAdapter: FileListAdapter? = null
    private var fileRecyclerView: RecyclerView? = null
    private lateinit var viewModel: FileListViewModel

    // region Fragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(FileListViewModel::class.java)
        subscribeUi()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.file_list_fragment, container, false)

        fileAdapter = FileListAdapter(context)
        fileRecyclerView = view?.findViewById(R.id.file_list)
        fileRecyclerView?.adapter = fileAdapter
        fileRecyclerView?.layoutManager = LinearLayoutManager(context)

        return view
    }

    // endregion

    private fun subscribeUi() {
        viewModel.files.observe(viewLifecycleOwner, Observer {
            fileAdapter?.setFiles(it)
        })
    }

    // region Static Constructor

    companion object {
        fun newInstance() = FileListFragment()
    }

    // endregion
}
