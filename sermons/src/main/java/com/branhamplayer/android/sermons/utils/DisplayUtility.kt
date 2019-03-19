package com.branhamplayer.android.sermons.utils

import android.content.Context
import android.util.DisplayMetrics

object DisplayUtility {

    // Thank you: https://stackoverflow.com/a/9563438/663604
    fun convertPxToDp(pixels: Int, context: Context) =
        pixels / (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}
