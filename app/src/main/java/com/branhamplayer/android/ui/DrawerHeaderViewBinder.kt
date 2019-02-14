package com.branhamplayer.android.ui

import androidx.appcompat.widget.AppCompatTextView
import butterknife.BindView
import com.branhamplayer.android.R
import com.branhamplayer.android.base.ui.ButterKnifeViewBinder

class DrawerHeaderViewBinder : ButterKnifeViewBinder() {

    @JvmField
    @BindView(R.id.navigation_drawer_header_email)
    var email: AppCompatTextView? = null

    @JvmField
    @BindView(R.id.navigation_drawer_header_name)
    var name: AppCompatTextView? = null
}
