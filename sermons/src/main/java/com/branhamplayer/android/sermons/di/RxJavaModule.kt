package com.branhamplayer.android.sermons.di

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

@Module
class RxJavaModule {

    companion object {
        const val BG = "BG"
        const val UI = "UI"
    }

    @Provides
    @Named(BG)
    fun provideBgScheduler() = Schedulers.io()

    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()

    @Provides
    @Named(UI)
    fun provideUiScheduler(): Scheduler = AndroidSchedulers.mainThread()
}
