package tech.oliver.branhamplayer.android.sermons.actions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import org.rekotlin.Action

sealed class DrawerAction : Action {
    data class CreateDrawerWithoutProfileAction(val activity: AppCompatActivity, val toolbar: Toolbar, val savedInstance: Bundle?, val selectedIndex: Int) : DrawerAction()
    data class PopulateDrawerWithProfileAction(val name: String, val email: String) : DrawerAction()
}
