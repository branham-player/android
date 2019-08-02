package com.branhamplayer.android.utils.navigation.navigators

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.branhamplayer.android.R

@Navigator.Name("module")
class ExternalModuleNavigator(
    private val activity: FragmentActivity
) : Navigator<ExternalModuleNavigator.Destination>() {

    override fun createDestination() = Destination(this)

    override fun navigate(destination: Destination, args: Bundle?, navOptions: NavOptions?, navigatorExtras: Extras?): NavDestination? {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(destination.uri))
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        intent.setPackage(activity.packageName)

        if (destination.clearBackStack) intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

        activity.startActivity(intent)
        return null // Managed by the new module's activity
    }

    override fun popBackStack() = true // Managed by the new module's activity

    @NavDestination.ClassType(FragmentActivity::class)
    class Destination(navigator: Navigator<out NavDestination>) : NavDestination(navigator) {

        var clearBackStack = false
        var uri: String = ""

        override fun onInflate(context: Context, attrs: AttributeSet) {
            super.onInflate(context, attrs)

            context.withStyledAttributes(attrs, R.styleable.ExternalModuleNavigator, 0, 0) {
                clearBackStack = getBoolean(R.styleable.ExternalModuleNavigator_clearBackStack, false)
                uri = getString(R.styleable.ExternalModuleNavigator_uri) ?: ""
            }
        }
    }
}