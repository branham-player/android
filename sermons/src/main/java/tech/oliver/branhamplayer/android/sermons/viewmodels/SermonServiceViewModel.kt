package tech.oliver.branhamplayer.android.sermons.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.orhanobut.logger.Logger
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.MaybeObserver
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import tech.oliver.branhamplayer.android.sermons.database.SermonsDatabase
import tech.oliver.branhamplayer.android.sermons.database.recent.RecentEntity
import tech.oliver.branhamplayer.android.sermons.database.times.TimesEntity

class SermonServiceViewModel(
        context: Context,
        private val database: SermonsDatabase = SermonsDatabase.newInstance(context),
        private val bg: Scheduler = Schedulers.io(),
        private val ui: Scheduler = AndroidSchedulers.mainThread()
) : ViewModel() {

    fun getMostRecentSermonAsync(callback: ((mediaId: String?) -> Unit)) {
        database.recentDao()
                .fetchSermon()
                .subscribeOn(bg)
                .observeOn(ui)
                .subscribe(object : MaybeObserver<RecentEntity> {
                    override fun onComplete() {
                        Logger.i("Could not find a most recent sermon")

                        callback.invoke(null)
                    }

                    override fun onError(e: Throwable) {
                        Logger.e("Encountered an error when locating a most recent sermon")
                        Logger.e("Error: ${e.message}")

                        callback.invoke(null)
                    }

                    override fun onSuccess(record: RecentEntity) {
                        Logger.d("Found a most recent sermon")
                        Logger.d("Sermon: ${record.mediaId}")

                        callback.invoke(record.mediaId)
                    }

                    override fun onSubscribe(d: Disposable) = Unit
                })
    }

    fun getPausedDurationAsync(mediaId: String?, callback: ((duration: Int) -> Unit)) {
        database.timesDao()
                .fetchSermon(mediaId.toString())
                .subscribeOn(bg)
                .observeOn(ui)
                .subscribe(object : MaybeObserver<TimesEntity> {
                    override fun onComplete() {
                        Logger.i("Could not find a paused duration for: $mediaId")
                        Logger.i("Defaulting to the beginning")

                        callback.invoke(0)
                    }

                    override fun onError(e: Throwable) {
                        Logger.e("Encountered an error when locating a paused duration for: $mediaId")
                        Logger.e("Error: ${e.message}")
                        Logger.e("Defaulting to the beginning")

                        callback.invoke(0)
                    }

                    override fun onSuccess(record: TimesEntity) {
                        Logger.d("Found a paused duration for: $mediaId")
                        Logger.d("Time: ${record.time}")

                        callback.invoke(record.time)
                    }

                    override fun onSubscribe(d: Disposable) = Unit
                })
    }

    fun saveMostRecentSermon(mediaId: String?) {
        Completable.fromAction {
            database.recentDao().upsert(RecentEntity(
                    mediaId = mediaId.toString()
            ))
        }.subscribeOn(bg)
                .observeOn(ui)
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        Logger.d("Saved a most recent sermon")
                        Logger.d("Sermon: $mediaId")
                    }

                    override fun onError(e: Throwable) {
                        Logger.e("Encountered an error when saving a most recent sermon for: $mediaId")
                        Logger.e("Error: ${e.message}")
                    }

                    override fun onSubscribe(d: Disposable) = Unit
                })
    }

    fun savePausedDuration(mediaId: String?, time: Int) {
        Completable.fromAction {
            database.timesDao().upsert(TimesEntity(
                    mediaId = mediaId.toString(),
                    time = time
            ))
        }.subscribeOn(bg)
                .observeOn(ui)
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        Logger.d("Saved a paused duration for: $mediaId")
                        Logger.d("Time: $time")
                    }

                    override fun onError(e: Throwable) {
                        Logger.e("Encountered an error when saving a paused duration for: $mediaId")
                        Logger.e("Error: ${e.message}")
                    }

                    override fun onSubscribe(d: Disposable) = Unit
                })
    }
}