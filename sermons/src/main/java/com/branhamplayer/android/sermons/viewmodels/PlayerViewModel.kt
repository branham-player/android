package com.branhamplayer.android.sermons.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.branhamplayer.android.sermons.database.SermonsDatabase
import com.branhamplayer.android.sermons.database.recent.RecentEntity
import com.branhamplayer.android.sermons.database.times.TimesEntity
import com.branhamplayer.android.utils.logging.Loggly
import com.branhamplayer.android.utils.logging.LogglyConstants.Tags.PLAYER_VIEW_MODEL
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PlayerViewModel(
    context: Context,
    private val database: SermonsDatabase = SermonsDatabase.newInstance(context),
    private val disposable: CompositeDisposable = CompositeDisposable(),
    private val bg: Scheduler = Schedulers.io(),
    private val ui: Scheduler = AndroidSchedulers.mainThread()
) : ViewModel() {

    fun getMostRecentSermonAsync(): Single<String> = Single.create { subscriber ->
        val subscription = database.recentDao()
            .fetchSermon()
            .subscribeOn(bg)
            .observeOn(ui)
            .subscribe({
                Loggly.d(PLAYER_VIEW_MODEL, "Found a most recent sermon: ${it.mediaId}")
                subscriber.onSuccess(it.mediaId)
            }, {
                Loggly.e(
                    PLAYER_VIEW_MODEL,
                    it,
                    "Encountered an error when locating a most recent sermon: ${it.message}"
                )
                subscriber.onError(it)
            }, {
                Loggly.i(PLAYER_VIEW_MODEL, "Could not find a most recent sermon")
                subscriber.onError(Throwable("Could not find a most recent sermon"))
            })

        disposable.add(subscription)
    }

    fun getPausedDurationAsync(mediaId: String?): Single<Int> = Single.create { subscriber ->
        val subscription = database.timesDao()
            .fetchSermon(mediaId.toString())
            .subscribeOn(bg)
            .observeOn(ui)
            .subscribe({
                Loggly.d(PLAYER_VIEW_MODEL, "Found a paused startingTime for: $mediaId, time: ${it.time}")
                subscriber.onSuccess(it.time)
            }, {
                Loggly.e(
                    PLAYER_VIEW_MODEL,
                    it,
                    "Encountered an error when locating a paused startingTime for: $mediaId"
                )
                subscriber.onSuccess(0)
            }, {
                Loggly.i(PLAYER_VIEW_MODEL, "Could not find a paused startingTime for: $mediaId")
                subscriber.onSuccess(0)
            })

        disposable.add(subscription)
    }

    fun release() {
        disposable.dispose()
    }

    fun saveMostRecentSermon(mediaId: String?) {
        val subscription = Completable.fromAction {
            database.recentDao().upsert(
                RecentEntity(
                    id = 1,
                    mediaId = mediaId.toString()
                )
            )
        }.subscribeOn(bg)
            .observeOn(ui)
            .subscribe({
                Loggly.d(PLAYER_VIEW_MODEL, "Saved a most recent sermon: $mediaId")
            }, {
                Loggly.e(PLAYER_VIEW_MODEL, it, "Encountered an error when saving a most recent sermon for: $mediaId")
            })

        disposable.add(subscription)
    }

    fun savePausedDuration(mediaId: String?, time: Int) {
        val subscription = Completable.fromAction {
            database.timesDao().upsert(
                TimesEntity(
                    mediaId = mediaId.toString(),
                    time = time
                )
            )
        }.subscribeOn(bg)
            .observeOn(ui)
            .subscribe({
                Loggly.d(PLAYER_VIEW_MODEL, "Saved a paused startingTime for: $mediaId, time: $time")
            }, {
                Loggly.e(PLAYER_VIEW_MODEL, it, "Encountered an error when saving a paused startingTime for: $mediaId")
            })

        disposable.add(subscription)
    }
}
