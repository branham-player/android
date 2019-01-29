package com.branhamplayer.android.mappers

import androidx.lifecycle.LiveData

interface Mapper<T, U> {
    fun map(input: List<T>?): LiveData<List<U>>?
}
