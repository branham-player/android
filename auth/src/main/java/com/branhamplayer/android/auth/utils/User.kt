package com.branhamplayer.android.auth.utils

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class User @Inject constructor(
    private val auth: FirebaseAuth
) {

    fun isLoggedIn() = auth.currentUser != null
}