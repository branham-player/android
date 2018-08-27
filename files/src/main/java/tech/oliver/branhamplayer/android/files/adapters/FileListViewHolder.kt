package tech.oliver.branhamplayer.android.files.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tech.oliver.branhamplayer.android.files.R
import tech.oliver.branhamplayer.android.files.models.FileModel

class FileListViewHolder(
        itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val date: TextView = itemView.findViewById(R.id.song_date)
    private val name: TextView = itemView.findViewById(R.id.song_name)

    fun bind(file: FileModel) {
        date.text = file.formattedDate
        name.text = file.name
    }
}
