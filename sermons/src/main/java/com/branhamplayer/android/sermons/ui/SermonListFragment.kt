package com.branhamplayer.android.sermons.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

class SermonListFragment : Fragment(), KoinComponent, StoreSubscriber<SermonsState> {

    private var drawerToggle: ActionBarDrawerToggle? = null
    private val sermonAdapter: SermonsAdapter by inject { parametersOf(context) }

    private var drawer: NavigationView? = null
    private var drawerLayout: DrawerLayout? = null
    private var primaryToolbar: Toolbar? = null
    private var sermonsListToolbar: Toolbar? = null
    private var sermonsRecyclerView: RecyclerView? = null

    // region Controller

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.sermon_list_fragment, container, false)

        setUpComponents(view)
        sermonsStore.subscribe(this)

        activity?.let {
            val compatActivity = it as AppCompatActivity
            val isTablet = resources.getBoolean(com.branhamplayer.android.R.bool.is_tablet)

            sermonsStore.dispatch(DataAction.SetTitleAction(it, com.branhamplayer.android.R.string.navigation_sermons))
            sermonsStore.dispatch(PermissionAction.GetFileReadPermissionAction(compatActivity))

            if (isTablet) {
                setUpDrawer(it)
            }
        }

        return view
    }

    // endregion

    // region StoreSubscriber

    override fun newState(state: SermonsState) {

        val drawerUserEmail: AppCompatTextView? = activity?.findViewById(com.branhamplayer.android.R.id.navigation_drawer_header_email)
        val drawerUserName: AppCompatTextView? = activity?.findViewById(com.branhamplayer.android.R.id.navigation_drawer_header_name)

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
            sermonsListToolbar?.title = it
        }
    }

    // endregion

    private fun setUpComponents(view: View) {
        val linearLayoutManager: LinearLayoutManager = get { parametersOf(context) }

        drawer = activity?.findViewById(R.id.navigation_drawer)
        drawerLayout = activity?.findViewById(R.id.navigation_drawer_layout)
        primaryToolbar = activity?.findViewById(R.id.primary_toolbar)
        sermonsListToolbar = activity?.findViewById(R.id.sermon_list_toolbar)
        sermonsRecyclerView = view.findViewById(R.id.sermon_list)

        sermonsRecyclerView?.adapter = sermonAdapter
        sermonsRecyclerView?.layoutManager = linearLayoutManager
    }

    private fun setUpDrawer(activity: AppCompatActivity) {

        drawerToggle = ActionBarDrawerToggle(
            activity,
            drawerLayout,
            primaryToolbar,
            com.branhamplayer.android.R.string.navigation_drawer_open,
            com.branhamplayer.android.R.string.navigation_drawer_close
        )

        drawerToggle?.let {
            drawerLayout?.addDrawerListener(it)
        }

        drawerToggle?.syncState()
        sermonsStore.dispatch(DrawerAction.SetSelectedItemAction(0))
        sermonsStore.dispatch(ProfileAction.GetUserProfileAction(activity))
    }
}
