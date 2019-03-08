package com.branhamplayer.android.sermons.ui

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.branhamplayer.android.R as RBase
import com.branhamplayer.android.sermons.R
import com.branhamplayer.android.sermons.actions.*
import com.branhamplayer.android.sermons.di.DaggerInjector
import com.branhamplayer.android.sermons.store.sermonsStore
import com.branhamplayer.android.sermons.states.SermonsState
import com.branhamplayer.android.ui.DrawerHeaderViewBinder
import com.google.android.material.navigation.NavigationView
import org.rekotlin.StoreSubscriber
import javax.inject.Inject

class SermonsActivity : AppCompatActivity(), StoreSubscriber<SermonsState> {

    private var activityUnbinder: Unbinder? = null
    private var drawerToggle: ActionBarDrawerToggle? = null

    // region DI

    @Inject
    lateinit var drawerHeaderBinder: DrawerHeaderViewBinder

    @Inject
    lateinit var sermonListFragment: SermonListFragment

    // endregion

    // region UI

    @BindView(R.id.navigation_drawer)
    lateinit var drawer: NavigationView

    @BindView(R.id.navigation_drawer_layout)
    lateinit var drawerLayout: DrawerLayout

    @BindView(R.id.primary_toolbar)
    lateinit var primaryToolbar: Toolbar

    @BindView(R.id.sermon_list_toolbar)
    lateinit var sermonsListToolbar: Toolbar

    // endregion

    // region AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sermons_activity)

        DaggerInjector.buildSermonsComponent(this).inject(this)

        activityUnbinder = ButterKnife.bind(this)
        sermonsStore.subscribe(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        supportFragmentManager.commit(allowStateLoss = true) {
            replace(R.id.sermon_list_container, sermonListFragment)
        }

        sermonsStore.dispatch(DataAction.SetTitleAction(RBase.string.navigation_sermons))
        sermonsStore.dispatch(PermissionAction.GetFileReadPermissionAction)
        sermonsStore.dispatch(RoutingAction.NavigateToNoSelectionAction)

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

        drawer.menu?.getItem(state.drawerItemSelectedIndex)?.isChecked = true

        state.profile?.let {
            drawerHeaderBinder.email?.text = it.email
            drawerHeaderBinder.name?.text = it.name
        }

        state.title?.let {
            sermonsListToolbar.title = it
        }
    }

    // endregion

    fun setDetailFragment(fragment: Fragment) {

        val isTablet = resources.getBoolean(RBase.bool.is_tablet)
        if (!isTablet) return

        supportFragmentManager.commit(allowStateLoss = true) {
            replace(R.id.sermons_details_container, fragment)
        }
    }

    fun setMasterFragment(fragment: Fragment) {
        supportFragmentManager.commit(allowStateLoss = true) {
            replace(R.id.sermon_list_container, fragment)
        }
    }

    private fun setUpDrawer() {

        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            primaryToolbar,
            RBase.string.navigation_drawer_open,
            RBase.string.navigation_drawer_close
        )

        drawerToggle?.let {
            drawerLayout.addDrawerListener(it)
        }

        drawerToggle?.syncState()

        if (drawer.headerCount == 1) {
            drawer.getHeaderView(0)?.let {
                drawerHeaderBinder.bind(it)
            }
        }

        sermonsStore.dispatch(DrawerAction.SetSelectedItemAction(0))
        sermonsStore.dispatch(ProfileAction.GetUserProfileAction)
    }
}
