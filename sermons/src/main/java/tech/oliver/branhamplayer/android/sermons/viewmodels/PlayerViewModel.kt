package tech.oliver.branhamplayer.android.sermons.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.orhanobut.logger.Logger
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.MaybeObserver
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import tech.oliver.branhamplayer.android.sermons.database.SermonsDatabase
import tech.oliver.branhamplayer.android.sermons.database.recent.RecentEntity
import tech.oliver.branhamplayer.android.sermons.database.times.TimesEntity

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
                    Logger.d("Found a most recent sermon")
                    Logger.d("Sermon: ${it.mediaId}")

                    subscriber.onSuccess(it.mediaId)
                }, {
                    Logger.e("Encountered an error when locating a most recent sermon")
                    Logger.e("Error: ${it.message}")

                    subscriber.onError(it)
                }, {
                    Logger.i("Could not find a most recent sermon")

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
                    Logger.d("Found a paused startingTime for: $mediaId")
                    Logger.d("Time: ${it.time}")

                    subscriber.onSuccess(it.time)
                }, {
                    Logger.e("Encountered an error when locating a paused startingTime for: $mediaId")
                    Logger.e("Error: ${it.message}")
                    Logger.e("Defaulting to the beginning")

                    subscriber.onError(it)
                }, {
                    Logger.i("Could not find a paused startingTime for: $mediaId")
                    Logger.i("Defaulting to the beginning")

                    subscriber.onError(Throwable("No paused startingTime found"))
                })

        disposable.add(subscription)
    }

    fun release() {
        disposable.dispose()
    }

    fun saveMostRecentSermon(mediaId: String?) {
        val subscription = Completable.fromAction {
            database.recentDao().upsert(RecentEntity(
                    mediaId = mediaId.toString()
            ))
        }.subscribeOn(bg)
                .observeOn(ui)
                .subscribe({
                    Logger.d("Saved a most recent sermon")
                    Logger.d("Sermon: $mediaId")
                }, {
                    Logger.e("Encountered an error when saving a most recent sermon for: $mediaId")
                    Logger.e("Error: ${it.message}")
                })

        disposable.add(subscription)
    }

    fun savePausedDuration(mediaId: String?, time: Int) {
        val subscription = Completable.fromAction {
            database.timesDao().upsert(TimesEntity(
                    mediaId = mediaId.toString(),
                    time = time
            ))
        }.subscribeOn(bg)
                .observeOn(ui)
                .subscribe({
                    Logger.d("Saved a paused startingTime for: $mediaId")
                    Logger.d("Time: $time")
                }, {
                    Logger.e("Encountered an error when saving a paused startingTime for: $mediaId")
                    Logger.e("Error: ${it.message}")
                })

        disposable.add(subscription)
    }
}
