package com.branhamplayer.android.data.database.permissions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface PermissionsDao {

    @Query("DELETE FROM permissions WHERE permission = :permission")
    fun clearDeniedPermanently(permission: String): Completable

    @Query("SELECT * FROM permissions WHERE permission = :permission")
    fun isDeniedPermanently(permission: String): Maybe<PermissionsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun markAsDeniedPermanently(permission: PermissionsEntity): Completable
}
