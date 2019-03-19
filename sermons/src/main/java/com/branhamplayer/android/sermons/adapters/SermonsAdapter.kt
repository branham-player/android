package com.branhamplayer.android.sermons.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.branhamplayer.android.sermons.R
import com.branhamplayer.android.sermons.models.SermonModel

class SermonsAdapter(
    context: Context?,
    private val inflater: LayoutInflater = LayoutInflater.from(context)
) : RecyclerView.Adapter<SermonViewHolder>() {

    private var sermons: List<SermonModel> = emptyList()

    // region RecyclerView.Adapter

    override fun onBindViewHolder(holder: SermonViewHolder, position: Int) {
        val sermon = sermons[position]

        holder.apply {
            bind(sermon)
            itemView.tag = sermon
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SermonViewHolder(inflater.inflate(R.layout.sermon_list_item_fragment, parent, false))

    override fun getItemCount() = sermons.size

    // endregion

    fun setSermons(newSermons: List<SermonModel>) {
        sermons = newSermons
    }
}
