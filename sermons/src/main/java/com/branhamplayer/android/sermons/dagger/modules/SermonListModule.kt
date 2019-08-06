package com.branhamplayer.android.sermons.dagger.modules

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.branhamplayer.android.dagger.modules.RxJavaModule
import com.branhamplayer.android.data.database.BranhamPlayerDatabase
import com.branhamplayer.android.sermons.mappers.SermonListMapper
import com.branhamplayer.android.sermons.middleware.SermonListMiddleware
import com.branhamplayer.android.sermons.reducers.SermonListReducer
import com.branhamplayer.android.sermons.repositories.SermonListRepository
import com.branhamplayer.android.sermons.ui.adapters.SermonListAdapter
import com.branhamplayer.android.sermons.utils.permissions.PermissionManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Named

@Module
class SermonListModule(private val context: Context) {

    @Provides
    fun provideDexter(
        activity: AppCompatActivity
    ): DexterBuilder.Permission = Dexter.withActivity(activity)

    @Provides
    fun providePermissionManager(
        activity: AppCompatActivity,
        database: BranhamPlayerDatabase,
        dexter: DexterBuilder.Permission,
        @Named(RxJavaModule.BG) bg: Scheduler,
        @Named(RxJavaModule.UI) ui: Scheduler
    ) = PermissionManager(
        activity = activity,
        database = database,
        dexter = dexter,
        bg = bg,
        ui = ui
    )

    @Provides
    fun provideSermonListAdapter() = SermonListAdapter(context)

    @Provides
    fun provideSermonListMiddleware(
        permissionManager: PermissionManager,
        @Named(RxJavaModule.BG) bg: Scheduler,
        @Named(RxJavaModule.UI) ui: Scheduler
    ) = SermonListMiddleware(
        permissionManager = permissionManager,
        bg = bg,
        ui = ui
    )

    @Provides
    fun provideSermonListReducer(
        sermonListMapper: SermonListMapper,
        repository: SermonListRepository
    ) = SermonListReducer(
        sermonListMapper = sermonListMapper,
        repository = repository
    )

    @Provides
    fun provideSermonListMapper() = SermonListMapper()

    @Provides
    fun provideSermonListRepository() = SermonListRepository()
}
