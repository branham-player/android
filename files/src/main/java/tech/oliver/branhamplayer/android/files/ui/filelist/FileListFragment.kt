package tech.oliver.branhamplayer.android.files.ui.filelist

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tech.oliver.branhamplayer.android.files.R

class FileListFragment : Fragment() {

    companion object {
        fun newInstance() = FileListFragment()
    }

    private lateinit var viewModel: FileListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.file_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FileListViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
