package com.branhamplayer.android.base.ui

import android.view.View
import butterknife.ButterKnife
import butterknife.Unbinder

abstract class ButterKnifeViewBinder {

    private var unbinder: Unbinder? = null

    fun bind(view: View) {
        unbinder = ButterKnife.bind(this, view)
    }

    fun unbind() {
        unbinder?.unbind()
    }
}
