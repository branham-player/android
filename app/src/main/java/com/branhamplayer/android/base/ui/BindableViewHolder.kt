package com.branhamplayer.android.base.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.branhamplayer.android.base.models.Model

abstract class BindableViewHolder<BindingModel : Model>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(model: BindingModel)
}
