package tech.oliver.branhamplayer.android.sermons.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tech.oliver.branhamplayer.android.sermons.R
import tech.oliver.branhamplayer.android.sermons.models.SermonModel

class SermonViewHolder(
        itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val date: TextView = itemView.findViewById(R.id.sermon_date)
    private val name: TextView = itemView.findViewById(R.id.sermon_name)

    fun bind(sermon: SermonModel) {
        date.text = sermon.formattedDate
        name.text = sermon.name
    }
}
