package com.branhamplayer.android.base.di

import dagger.Module
import dagger.Provides
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
    @Named(RxJavaModule.BG)
    fun getBgScheduler() = Schedulers.io()

    @Provides
    fun getCompositeDisposable() = CompositeDisposable()

    @Provides
    @Named(RxJavaModule.UI)
    fun getUiScheduler() = AndroidSchedulers.mainThread()
}
