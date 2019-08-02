package com.branhamplayer.android.utils.navigation

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.plusAssign
import com.branhamplayer.android.auth.navigation.LoginNavigator
import com.branhamplayer.android.utils.navigation.navigators.ExternalModuleNavigator

class StartupNavHostFragment : NavHostFragment() {

    override fun onCreateNavController(navController: NavController) {
        super.onCreateNavController(navController)

        activity?.let {
            navController.navigatorProvider += ExternalModuleNavigator(it)
            navController.navigatorProvider += LoginNavigator(it)
        }
    }
}