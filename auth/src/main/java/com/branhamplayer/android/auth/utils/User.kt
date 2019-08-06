package com.branhamplayer.android.auth.utils

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class User @Inject constructor() {

    data class Profile (
        val email: String?,
        val name: String?
    )

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getProfile() = auth.currentUser.let {
        Profile(
            email = it?.email,
            name = it?.displayName
        )
    }

    fun isLoggedIn() = auth.currentUser != null
}