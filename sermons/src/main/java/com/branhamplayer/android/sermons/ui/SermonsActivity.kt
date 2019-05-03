package com.branhamplayer.android.sermons.ui

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.branhamplayer.android.sermons.R
import com.branhamplayer.android.sermons.actions.AuthenticationAction
import com.branhamplayer.android.sermons.actions.DrawerAction
import com.branhamplayer.android.sermons.actions.SermonListAction
import com.branhamplayer.android.sermons.di.DaggerInjector
import com.branhamplayer.android.sermons.states.SermonsState
import com.branhamplayer.android.sermons.store.sermonsStore
import com.branhamplayer.android.ui.DrawerHeaderViewBinder
import com.google.android.material.navigation.NavigationView
import org.rekotlin.StoreSubscriber
import javax.inject.Inject
import com.branhamplayer.android.R as RBase

class SermonsActivity : AppCompatActivity(), StoreSubscriber<SermonsState> {

    private var activityUnbinder: Unbinder? = null
    private var drawerToggle: ActionBarDrawerToggle? = null

    // region Components

    @JvmField
    @BindView(R.id.drawer)
    var drawer: NavigationView? = null

    @JvmField
    @BindView(R.id.drawer_layout)
    var drawerLayout: DrawerLayout? = null

    @JvmField
    @BindView(R.id.toolbar)
    var toolbar: Toolbar? = null

    // endregion

    // region DI

    @Inject
    lateinit var drawerHeaderBinder: DrawerHeaderViewBinder

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

        sermonsStore.dispatch(SermonListAction.GetFileReadPermissionAction)

        setUpToolbar()
        setUpDrawer()
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
    }

    // endregion

    private fun setUpDrawer() {

        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
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
        sermonsStore.dispatch(AuthenticationAction.GetUserAuthenticationAction)
    }

    private fun setUpToolbar() {
        val controller = findNavController(R.id.sermon_navigation_host)
        val appBarConfiguration = AppBarConfiguration(controller.graph)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        toolbar.setupWithNavController(controller, appBarConfiguration)
    }
}
