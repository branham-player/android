package tech.oliver.branhamplayer.filelist

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.file_list_fragment.fileList
import org.rekotlin.StoreSubscriber
import tech.oliver.branhamplayer.R
import tech.oliver.branhamplayer.filelist.recyclerview.FileListAdapter
import tech.oliver.branhamplayer.filelist.redux.PopulateFiles
import tech.oliver.branhamplayer.root.AppState
import tech.oliver.branhamplayer.store



class FileListFragment : Fragment(), StoreSubscriber<AppState> {

    var adapter: FileListAdapter? = null

    // region Lifecycle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.file_list_fragment, container, false)
        adapter = FileListAdapter(context)

        store.subscribe(this)
        store.dispatch(PopulateFiles())

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        store.unsubscribe(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layout = LinearLayoutManager(context)
        val divider = DividerItemDecoration(context, layout.orientation)

        fileList.adapter = adapter
        fileList.layoutManager = layout
        fileList.addItemDecoration(divider)
    }

    // endregion

    // region StoreSubscriber<AppState>

    override fun newState(state: AppState) {
        adapter?.updateData(state.fileListState?.files)
        adapter?.notifyDataSetChanged()
    }

    // endregion
}
