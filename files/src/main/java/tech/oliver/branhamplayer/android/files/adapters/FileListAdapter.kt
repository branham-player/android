package tech.oliver.branhamplayer.android.files.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tech.oliver.branhamplayer.android.files.R
import tech.oliver.branhamplayer.android.files.models.FileModel

class FileListAdapter(
        context: Context?,
        private val inflater: LayoutInflater = LayoutInflater.from(context)
) : RecyclerView.Adapter<FileListViewHolder>() {

    private var files: List<FileModel> = emptyList()

    // region RecyclerView.Adapter

    override fun onBindViewHolder(holder: FileListViewHolder, position: Int) {
        val file = files[position]

        holder.apply {
            bind(file)
            itemView.tag = file
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileListViewHolder {
        val view = inflater.inflate(R.layout.file_list_item_fragment, parent, false)
        return FileListViewHolder(view)
    }

    override fun getItemCount() = files.size

    // endregion

    fun setFiles(filesUpdate: List<FileModel>) {
        files = filesUpdate
    }
}
