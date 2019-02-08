package com.branhamplayer.android.sermons.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluelinelabs.conductor.RestoreViewOnCreateController
import com.branhamplayer.android.R as RBase
import com.branhamplayer.android.sermons.R
import com.branhamplayer.android.sermons.actions.DataAction
import com.branhamplayer.android.sermons.actions.DrawerAction
import com.branhamplayer.android.sermons.actions.PermissionAction
import com.branhamplayer.android.sermons.actions.ProfileAction
import com.branhamplayer.android.sermons.adapters.SermonsAdapter
import com.branhamplayer.android.sermons.shared.sermonsStore
import com.branhamplayer.android.sermons.states.SermonsState
import com.google.android.material.navigation.NavigationView
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import org.koin.standalone.inject
import org.rekotlin.StoreSubscriber

class SermonsController : RestoreViewOnCreateController(), KoinComponent, StoreSubscriber<SermonsState> {

    private var drawerToggle: ActionBarDrawerToggle? = null
    private val sermonAdapter: SermonsAdapter by inject { parametersOf(applicationContext) }

    private var drawer: NavigationView? = null
    private var drawerLayout: DrawerLayout? = null
    private var primaryToolbar: Toolbar? = null
    private var secondaryToolbar: Toolbar? = null
    private var sermonsRecyclerView: RecyclerView? = null

    // region Controller

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        val view = inflater.inflate(R.layout.sermons_controller, container, false)
        val linearLayoutManager: LinearLayoutManager = get { parametersOf(applicationContext) }

        sermonsStore.subscribe(this)

        drawer = activity?.findViewById(R.id.navigation_drawer)
        drawerLayout = activity?.findViewById(R.id.navigation_drawer_layout)
        primaryToolbar = activity?.findViewById(R.id.primary_toolbar)
        secondaryToolbar = activity?.findViewById(R.id.sermon_list_toolbar)
        sermonsRecyclerView = view?.findViewById(R.id.sermon_list)

        sermonsRecyclerView?.adapter = sermonAdapter
        sermonsRecyclerView?.layoutManager = linearLayoutManager

        activity?.let {
            val compatActivity = it as AppCompatActivity
            val isTablet = resources?.getBoolean(RBase.bool.is_tablet) == true

            sermonsStore.dispatch(DataAction.SetTitleAction(it, RBase.string.navigation_sermons))
            sermonsStore.dispatch(PermissionAction.GetFileReadPermissionAction(compatActivity))

            if (isTablet) {
                drawerToggle = ActionBarDrawerToggle(
                    activity,
                    drawerLayout,
                    primaryToolbar,
                    RBase.string.navigation_drawer_open,
                    RBase.string.navigation_drawer_close
                )

                drawerToggle?.let {
                    drawerLayout?.addDrawerListener(it)
                }

                drawerToggle?.syncState()

                sermonsStore.dispatch(DrawerAction.SetSelectedItemAction(0))
                sermonsStore.dispatch(ProfileAction.GetUserProfileAction(it))
            }
        }

        return view
    }

    // endregion

    // region StoreSubscriber

    override fun newState(state: SermonsState) {

        val drawerUserEmail: AppCompatTextView? = activity?.findViewById(RBase.id.navigation_drawer_header_email)
        val drawerUserName: AppCompatTextView? = activity?.findViewById(RBase.id.navigation_drawer_header_name)

        drawer?.menu?.getItem(state.drawerItemSelectedIndex)?.isChecked = true

        state.profile?.let {
            drawerUserEmail?.text = it.email
            drawerUserName?.text = it.name
        }

        state.sermonList?.let {
            sermonAdapter.setSermons(it)
            sermonsRecyclerView?.adapter?.notifyDataSetChanged()
        }

        state.title?.let {
            secondaryToolbar?.title = it
        }
    }

    // endregion
}
