package com.branhamplayer.android.sermons.adapters

import android.view.View
import android.widget.TextView
import com.branhamplayer.android.base.ui.BindableViewHolder
import com.branhamplayer.android.sermons.R
import com.branhamplayer.android.sermons.actions.PlayerAction
import com.branhamplayer.android.sermons.models.SermonModel
import com.branhamplayer.android.sermons.store.sermonsStore

class SermonViewHolder(itemView: View) : BindableViewHolder<SermonModel>(itemView), View.OnClickListener {

    private val date: TextView = itemView.findViewById(R.id.sermon_date)
    private val name: TextView = itemView.findViewById(R.id.sermon_name)

    private var sermonModel: SermonModel? = null

    init {
        itemView.setOnClickListener(this)
    }

    // region BindableViewHolder

    override fun bind(model: SermonModel) {
        date.text = model.formattedDate
        name.text = model.name

        sermonModel = model
    }

    // endregion

    // region View.OnClickListener

    override fun onClick(view: View?) {
        sermonModel?.let {
            sermonsStore.dispatch(PlayerAction.NavigateToPlayerAction(it))
        }
    }

    // endregion
}
