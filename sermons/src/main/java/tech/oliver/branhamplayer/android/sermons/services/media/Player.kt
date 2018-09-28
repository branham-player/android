package tech.oliver.branhamplayer.android.sermons.services.media

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.PowerManager
import android.support.v4.media.session.PlaybackStateCompat
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tech.oliver.branhamplayer.android.sermons.R
import tech.oliver.branhamplayer.android.sermons.SermonConstants
import tech.oliver.branhamplayer.android.services.logging.Loggly
import tech.oliver.branhamplayer.android.services.logging.LogglyConstants.Tags.PLAYER

class Player(
        private val context: Context,
        private val callback: Callback,
        private val mediaPlayer: MediaPlayer = MediaPlayer(),
        private val disposable: CompositeDisposable = CompositeDisposable(),
        private val bg: Scheduler = Schedulers.io(),
        private val ui: Scheduler = AndroidSchedulers.mainThread()
) : MediaPlayer.OnCompletionListener {

    private var currentSermon: String? = null
    private var state = PlaybackStateCompat.STATE_NONE

    init {
        val attributes = AudioAttributes.Builder().apply {
            setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            setLegacyStreamType(AudioManager.STREAM_MUSIC)
            setUsage(AudioAttributes.USAGE_MEDIA)
        }

        mediaPlayer.reset()
        mediaPlayer.setAudioAttributes(attributes.build())
        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
    }

    // region Player Controls

    fun pause() {
        if (currentSermon == null) return
        if (state != PlaybackStateCompat.STATE_PLAYING) return

        state = PlaybackStateCompat.STATE_PAUSED

        callback.onPlayerStateChanged(buildState(state, mediaPlayer.currentPosition), currentSermon, mediaPlayer.currentPosition)

        if (mediaPlayer.isPlaying) mediaPlayer.pause()
    }

    fun play(pathToSermon: String?, startingTime: Int) {
        if (pathToSermon == null) return
        if (state == PlaybackStateCompat.STATE_PLAYING) return

        val subscription = setCurrentWithoutPlaying(pathToSermon, startingTime)
                .subscribeOn(bg)
                .observeOn(ui)
                .subscribe { preparedPlayer ->
                    state = PlaybackStateCompat.STATE_PLAYING

                    callback.onPlayerStateChanged(buildState(state, startingTime), currentSermon, startingTime)

                    if (!mediaPlayer.isPlaying) preparedPlayer.start()
                }

        disposable.add(subscription)
    }

    fun release() {
        pause()
        disposable.dispose()
        mediaPlayer.release()
    }

    fun restartCurrentFromBeginning() {
        pause()
        play(currentSermon, 0)
    }

    fun resumeCurrent() {
        play(currentSermon, mediaPlayer.currentPosition)
    }

    fun setCurrentWithoutPlaying(pathToSermon: String?, startingTime: Int = 0): Single<MediaPlayer> = Single.create { subscriber ->
        currentSermon = pathToSermon

        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(pathToSermon)

            mediaPlayer.setOnPreparedListener { preparedPlayer ->
                state = PlaybackStateCompat.STATE_PAUSED

                callback.onPlayerStateChanged(buildState(state, startingTime), currentSermon, startingTime)
                preparedPlayer.seekTo(startingTime)
                subscriber.onSuccess(preparedPlayer)
            }

            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            Loggly.e(PLAYER, e, "Could not load the sermon: $pathToSermon")
            subscriber.onError(e)
        }
    }

    fun setVolume(volume: Float) {
        mediaPlayer.setVolume(volume, volume)
    }

    // endregion

    // region MediaPlayer.OnCompletionListener

    override fun onCompletion(player: MediaPlayer?) {
        callback.onSermonCompleted(currentSermon)
    }

    // endregion

    // region Private Methods

    private fun buildState(state: Int, currentTime: Int) = PlaybackStateCompat
            .Builder()
            .setActions(PlaybackStateCompat.ACTION_PAUSE or
                    PlaybackStateCompat.ACTION_PLAY or
                    PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            )
            .setState(
                    state,
                    currentTime.toLong(),
                    1f
            )
            .addCustomAction(
                    PlaybackStateCompat.CustomAction.Builder(
                            SermonConstants.Player.CustomActions.Restart,
                            context.getString(R.string.player_restart),
                            R.drawable.restart
                    ).build()
            )
            .build()

    // endregion

    // region Callback Definition

    interface Callback {
        fun onPlayerStateChanged(state: PlaybackStateCompat, mediaId: String?, duration: Int)
        fun onSermonCompleted(mediaId: String?)
    }

    // endregion
}
