package com.branhamplayer.android.sermons.ui

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.branhamplayer.android.R

sealed class DrawerItemsConstants {

    object Divider : DrawerItemsConstants()

    sealed class MenuItem(
            @DrawableRes val icon: Int,
            val id: Int,
            @StringRes val name: Int
    ) : DrawerItemsConstants() {

        class Account : MenuItem(
                icon = R.drawable.ic_account,
                id = IDs.Account,
                name = R.string.navigation_account
        )

        class Downloads : MenuItem(
                icon = R.drawable.ic_downloads,
                id = IDs.Downloads,
                name = R.string.navigation_downloads
        )

        class Logout : MenuItem(
                icon = R.drawable.ic_logout,
                id = IDs.Logout,
                name = R.string.navigation_logout
        )

        class Sermons : MenuItem(
                icon = R.drawable.ic_sermons,
                id = IDs.Sermons,
                name = R.string.navigation_sermons
        )
    }

    object Colors {
        @ColorRes val Selected = R.color.colorPrimary
        @ColorRes val Unselected = android.R.color.black
    }

    object IDs {
        const val Sermons = 0
        const val Downloads = 1
        const val Account = 2
        const val Logout = 3
    }
}
