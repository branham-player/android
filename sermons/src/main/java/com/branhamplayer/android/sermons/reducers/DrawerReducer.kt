package com.branhamplayer.android.sermons.reducers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.branhamplayer.android.sermons.R
import com.branhamplayer.android.sermons.actions.DrawerAction
import com.branhamplayer.android.sermons.states.SermonsState
import com.branhamplayer.android.sermons.ui.DrawerItemsConstants
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import org.koin.standalone.StandAloneContext

class DrawerReducer {
    companion object {

        fun reduce(action: DrawerAction, oldState: SermonsState) = when (action) {
            is DrawerAction.CreateDrawerWithoutProfileAction -> createDrawer(oldState, action.activity, action.toolbar, action.savedInstance, action.selectedIndex)
            is DrawerAction.PopulateDrawerWithProfileAction -> populateDrawerWithProfile(action.name, action.email, oldState)
        }

        private fun createDrawer(oldState: SermonsState, activity: AppCompatActivity, toolbar: Toolbar, savedInstance: Bundle?, selectedIndex: Int): SermonsState {

            val accountHeaderBuilder: AccountHeaderBuilder = StandAloneContext.getKoin().koinContext.get()
            val drawerBuilder: DrawerBuilder = StandAloneContext.getKoin().koinContext.get()

            val accountMenuItem: DrawerItemsConstants.MenuItem.Account = StandAloneContext.getKoin().koinContext.get()
            val divider: DrawerItemsConstants.Divider = StandAloneContext.getKoin().koinContext.get()
            val downloadsMenuItem: DrawerItemsConstants.MenuItem.Downloads = StandAloneContext.getKoin().koinContext.get()
            val logoutMenuItem: DrawerItemsConstants.MenuItem.Logout = StandAloneContext.getKoin().koinContext.get()
            val sermonsMenuItem: DrawerItemsConstants.MenuItem.Sermons = StandAloneContext.getKoin().koinContext.get()

            val menuItems = listOf(sermonsMenuItem, downloadsMenuItem, accountMenuItem, divider, logoutMenuItem).map {

                val primaryDrawerItem: PrimaryDrawerItem = StandAloneContext.getKoin().koinContext.get()

                if (it is DrawerItemsConstants.MenuItem) {
                    if (it.id == selectedIndex) {
                        primaryDrawerItem
                                .withName(it.name)
                                .withIcon(it.icon)
                                .withIconTintingEnabled(true)
                                .withIdentifier(it.id.toLong())
                                .withSelectedIconColorRes(DrawerItemsConstants.Colors.Selected)
                                .withTextColorRes(DrawerItemsConstants.Colors.Selected)
                    } else {
                        primaryDrawerItem
                                .withName(it.name)
                                .withIcon(it.icon)
                                .withIconTintingEnabled(true)
                                .withIdentifier(it.id.toLong())
                                .withSelectedIconColorRes(DrawerItemsConstants.Colors.Unselected)
                                .withTextColorRes(DrawerItemsConstants.Colors.Unselected)
                    }
                } else {
                    val dividerDrawerItem: DividerDrawerItem = StandAloneContext.getKoin().koinContext.get()
                    dividerDrawerItem
                }
            }

            val accountHeader = accountHeaderBuilder
                    .withActivity(activity)
                    .withSavedInstance(savedInstance)
                    .withSelectionListEnabledForSingleProfile(false)
                    .build()

            val drawer = drawerBuilder
                    .withAccountHeader(accountHeader)
                    .withActionBarDrawerToggleAnimated(true)
                    .withActivity(activity)
                    .withDrawerItems(menuItems)
                    .withToolbar(toolbar)
                    .withSavedInstance(savedInstance)
                    .withSelectedItemByPosition(selectedIndex)
                    .build()

            return oldState.copy(
                    drawer = drawer,
                    drawerAccountHeader = accountHeader,
                    drawerItemSelectedIndex = selectedIndex
            )
        }

        private fun populateDrawerWithProfile(name: String, email: String, oldState: SermonsState): SermonsState {

            val profileDrawerItem: ProfileDrawerItem = StandAloneContext.getKoin().koinContext.get()

            oldState.drawerAccountHeader?.addProfile(
                    profileDrawerItem.withEmail(email).withName(name).withTextColorRes(R.color.colorAccent),
                    0
            )

            return oldState
        }
    }
}
