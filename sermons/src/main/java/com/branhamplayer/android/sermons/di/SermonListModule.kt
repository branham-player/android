package com.branhamplayer.android.sermons.di

import android.content.Context
import com.branhamplayer.android.sermons.adapters.SermonsAdapter
import com.branhamplayer.android.sermons.mappers.SermonMapper
import com.branhamplayer.android.sermons.middleware.SermonsListMiddleware
import com.branhamplayer.android.sermons.reducers.SermonListReducer
import com.branhamplayer.android.sermons.repositories.SermonsRepository
import com.branhamplayer.android.sermons.ui.SermonListFragment
import com.branhamplayer.android.sermons.ui.SermonsActivity
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Named

@Module
class SermonListModule {

    @Provides
    fun provideSermonsAdapter(context: Context) = SermonsAdapter(context)

    @Provides
    fun provideSermonListFragment() = SermonListFragment()

    @Provides
    fun provideSermonListMiddleware(
        activity: SermonsActivity,
        @Named(RxJavaModule.BG) bg: Scheduler,
        @Named(RxJavaModule.UI) ui: Scheduler
    ) = SermonsListMiddleware(
        activity, bg, ui
    )

    @Provides
    fun provideSermonListReducer(mapper: SermonMapper, repository: SermonsRepository) =
        SermonListReducer(mapper, repository)

    @Provides
    fun provideSermonMapper() = SermonMapper()

    @Provides
    fun provideSermonsRepository() = SermonsRepository()
}