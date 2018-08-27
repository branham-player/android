package tech.oliver.branhamplayer.android.base

import androidx.lifecycle.LiveData

interface Mapper<T, U> {
    fun map(input: List<T>?): LiveData<List<U>>?
}
