package tech.oliver.branhamplayer.filelist.recyclerview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import tech.oliver.branhamplayer.filelist.FileListViewModel

class FileListAdapter(
        context: Context?,
        private var viewModels: List<FileListViewModel> = emptyList(),
        private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
) : RecyclerView.Adapter<FileListViewHolder>() {

    fun updateData(data: List<FileListViewModel>?) {
        viewModels = data ?: emptyList()
    }

    override fun onBindViewHolder(holder: FileListViewHolder, position: Int) =
            holder.bind(viewModels[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            FileListViewHolder(layoutInflater, parent)

    override fun getItemCount() = viewModels.count()
}
