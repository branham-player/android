package com.branhamplayer.android.sermons.ui.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.branhamplayer.android.sermons.R
import com.branhamplayer.android.sermons.models.SermonModel

class SermonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val date: TextView = itemView.findViewById(R.id.sermon_date)
    private val name: TextView = itemView.findViewById(R.id.sermon_name)

    fun bind(sermon: SermonModel) {
        date.text = sermon.formattedDate
        name.text = sermon.name
    }
}
