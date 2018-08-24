package tech.oliver.branhamplayer.filelist.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.file_list_row_fragment.messageDate
import kotlinx.android.synthetic.main.file_list_row_fragment.messageTitle
import tech.oliver.branhamplayer.R
import tech.oliver.branhamplayer.filelist.FileListViewModel

class FileListViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        override val containerView: View = inflater.inflate(R.layout.file_list_row_fragment, parent, false)
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(file: FileListViewModel) {
        messageDate.text = file.formattedDate
        messageTitle.text = file.name
    }
}
