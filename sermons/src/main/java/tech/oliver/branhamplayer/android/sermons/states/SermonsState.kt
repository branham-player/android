package tech.oliver.branhamplayer.android.sermons.states

import com.auth0.android.result.UserProfile
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.MiniDrawer
import org.rekotlin.StateType
import tech.oliver.branhamplayer.android.sermons.models.SermonModel

data class SermonsState(
        val drawer: Drawer? = null,
        val drawerAccountHeader: AccountHeader? = null,
        val drawerItemSelectedIndex: Int = 0,
        val profile: UserProfile? = null,
        val sermonList: List<SermonModel>? = null,
        val title: String? = null
) : StateType
