package com.branhamplayer.android.utils.navigation

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.plusAssign
import com.branhamplayer.android.auth.navigation.LoginNavigator

class StartupNavHostFragment : NavHostFragment() {

    override fun onCreateNavController(navController: NavController) {
        super.onCreateNavController(navController)

        activity?.let {
            navController.navigatorProvider += LoginNavigator(it)
        }
    }
}