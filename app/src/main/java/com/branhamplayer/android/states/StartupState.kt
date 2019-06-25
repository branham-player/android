package com.branhamplayer.android.states

import org.rekotlin.StateType

data class StartupState(
    val metadataAvailable: Boolean = true,
    val message: String? = null,
    val minimumVersionMet: Boolean = true,
    val platformAvailable: Boolean = true,
    val ranPreflightChecklistSuccessfully: Boolean = false
) : StateType
