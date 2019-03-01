package com.branhamplayer.android.sermons.ui

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.commit
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.branhamplayer.android.R as RBase
import com.branhamplayer.android.sermons.R
import com.branhamplayer.android.sermons.actions.DataAction
import com.branhamplayer.android.sermons.actions.DrawerAction
import com.branhamplayer.android.sermons.actions.PermissionAction
import com.branhamplayer.android.sermons.actions.ProfileAction
import com.branhamplayer.android.sermons.di.DaggerInjector
import com.branhamplayer.android.sermons.shared.sermonsModule
import com.branhamplayer.android.sermons.store.sermonsStore
import com.branhamplayer.android.sermons.states.SermonsState
import com.branhamplayer.android.ui.DrawerHeaderViewBinder
import com.google.android.material.navigation.NavigationView
import org.koin.android.ext.android.inject
import org.koin.standalone.StandAloneContext
import org.rekotlin.StoreSubscriber

class SermonsActivity : AppCompatActivity(), StoreSubscriber<SermonsState> {

    private var drawerToggle: ActionBarDrawerToggle? = null
    private val sermonListFragment: SermonListFragment by inject()

    private var activityUnbinder: Unbinder? = null
    private val drawerHeaderBinder: DrawerHeaderViewBinder by inject()

    // region Components

    @JvmField
    @BindView(R.id.navigation_drawer)
    var drawer: NavigationView? = null

    @JvmField
    @BindView(R.id.navigation_drawer_layout)
    var drawerLayout: DrawerLayout? = null

    @JvmField
    @BindView(R.id.primary_toolbar)
    var primaryToolbar: Toolbar? = null

    @JvmField
    @BindView(R.id.sermon_list_toolbar)
    var sermonsListToolbar: Toolbar? = null

    // endregion

    // region AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sermons_activity)

        DaggerInjector.buildSermonsComponent(this)

        activityUnbinder = ButterKnife.bind(this)
        sermonsStore.subscribe(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        supportFragmentManager.commit(allowStateLoss = true) {
            replace(R.id.sermon_list_container, sermonListFragment)
        }

        sermonsStore.dispatch(DataAction.SetTitleAction(RBase.string.navigation_sermons))
        sermonsStore.dispatch(PermissionAction.GetFileReadPermissionAction)

        val isTablet = resources.getBoolean(RBase.bool.is_tablet)

        if (isTablet) {
            setUpDrawer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        drawerHeaderBinder.unbind()
        activityUnbinder?.unbind()
    }

    // endregion

    // region StoreSubscriber

    override fun newState(state: SermonsState) {

        drawer?.menu?.getItem(state.drawerItemSelectedIndex)?.isChecked = true

        state.profile?.let {
            drawerHeaderBinder.email?.text = it.email
            drawerHeaderBinder.name?.text = it.name
        }

        state.title?.let {
            sermonsListToolbar?.title = it
        }
    }

    // endregion

    private fun setUpDrawer() {

        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            primaryToolbar,
            RBase.string.navigation_drawer_open,
            RBase.string.navigation_drawer_close
        )

        drawerToggle?.let {
            drawerLayout?.addDrawerListener(it)
        }

        drawerToggle?.syncState()

        if (drawer?.headerCount == 1) {
            drawer?.getHeaderView(0)?.let {
                drawerHeaderBinder.bind(it)
            }
        }

        sermonsStore.dispatch(DrawerAction.SetSelectedItemAction(0))
        sermonsStore.dispatch(ProfileAction.GetUserProfileAction)
    }
}
