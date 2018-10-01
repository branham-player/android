package tech.oliver.branhamplayer.android.sermons.services.media

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.PowerManager
import android.support.v4.media.session.PlaybackStateCompat
import io.reactivex.Completable
import tech.oliver.branhamplayer.android.sermons.R
import tech.oliver.branhamplayer.android.sermons.SermonConstants
import tech.oliver.branhamplayer.android.sermons.models.PlayerUpdateModel
import tech.oliver.branhamplayer.android.services.logging.Loggly
import tech.oliver.branhamplayer.android.services.logging.LogglyConstants.Tags.PLAYER

class Player(
        private val context: Context,
        private val callback: Callback,
        private val mediaPlayer: MediaPlayer = MediaPlayer()
) : MediaPlayer.OnCompletionListener {

    private var currentSermon: String? = null
    private var internalState = PlaybackStateCompat.STATE_NONE

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

        Loggly.d(PLAYER, "Successfully loaded the media player")
    }

    // region Player Controls

    fun pause(shouldNotifyStatusChange: Boolean = true) {
        if (currentSermon == null || internalState != PlaybackStateCompat.STATE_PLAYING) {
            Loggly.w(PLAYER, "Did not pause the sermon $currentSermon, internal state: $internalState, media player playing: ${mediaPlayer.isPlaying}")
            return
        }

        internalState = PlaybackStateCompat.STATE_PAUSED

        if (shouldNotifyStatusChange) {
            val update = PlayerUpdateModel(
                    state = internalState.toPlaybackState(),
                    oldSermon = PlayerUpdateModel.SermonState(
                            id = currentSermon,
                            duration = mediaPlayer.currentPosition
                    ),
                    newSermon = PlayerUpdateModel.SermonState(
                            id = currentSermon,
                            duration = mediaPlayer.currentPosition
                    )
            )

            callback.onPlayerStateChanged(update)
        }

        if (mediaPlayer.isPlaying) mediaPlayer.pause()

        Loggly.d(PLAYER, "Paused $currentSermon at ${mediaPlayer.currentPosition}")
    }

    fun play(pathToSermon: String?, startingTime: Int, shouldNotifyStatusChange: Boolean = true): Completable = Completable.create { subscriber ->
        if (pathToSermon == null || internalState == PlaybackStateCompat.STATE_PLAYING) {
            Loggly.w(PLAYER, "Did not play the sermon $currentSermon, internal state: $internalState, media player playing: ${mediaPlayer.isPlaying}")
            subscriber.onComplete()
            return@create
        }

        val oldSermon = currentSermon
        val oldSermonDuration = mediaPlayer.currentPosition

        currentSermon = pathToSermon

        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(pathToSermon)
            mediaPlayer.prepare()
            mediaPlayer.seekTo(startingTime)

            internalState = PlaybackStateCompat.STATE_PLAYING

            if (shouldNotifyStatusChange) {
                val update = PlayerUpdateModel(
                        state = internalState.toPlaybackState(),
                        oldSermon = PlayerUpdateModel.SermonState(
                                id = oldSermon,
                                duration = oldSermonDuration
                        ),
                        newSermon = PlayerUpdateModel.SermonState(
                                id = currentSermon,
                                duration = startingTime
                        )
                )

                callback.onPlayerStateChanged(update)
            }

            if (!mediaPlayer.isPlaying) mediaPlayer.start()

            Loggly.d(PLAYER, "Playing $currentSermon at $startingTime")
            subscriber.onComplete()
        } catch (e: Exception) {
            Loggly.e(PLAYER, e, "Could not load and play the sermon: $pathToSermon")
            subscriber.onError(e)
        }
    }

    fun release() {
        pause()
        mediaPlayer.release()
    }

    fun restartCurrentFromBeginning(): Completable = Completable
            .fromAction {
                pause(false)
            }
            .andThen {
                play(currentSermon, 0)
            }.andThen {
                Loggly.d(PLAYER, "Restarted $currentSermon")
            }

    fun resumeCurrent(): Completable = play(currentSermon, mediaPlayer.currentPosition)
            .andThen {
                Loggly.d(PLAYER, "Resuming $currentSermon at ${mediaPlayer.currentPosition}")
            }

    fun setCurrentWithoutPlaying(pathToSermon: String?, startingTime: Int = 0, shouldNotifyStatusChange: Boolean = true): Completable = Completable.create { subscriber ->
        val oldSermon = currentSermon
        val oldSermonDuration = mediaPlayer.currentPosition

        currentSermon = pathToSermon

        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(pathToSermon)
            mediaPlayer.prepare()
            mediaPlayer.seekTo(startingTime)

            internalState = PlaybackStateCompat.STATE_PAUSED

            if (shouldNotifyStatusChange) {
                val update = PlayerUpdateModel(
                        state = internalState.toPlaybackState(),
                        oldSermon = PlayerUpdateModel.SermonState(
                                id = oldSermon,
                                duration = oldSermonDuration
                        ),
                        newSermon = PlayerUpdateModel.SermonState(
                                id = currentSermon,
                                duration = startingTime
                        )
                )

                callback.onPlayerStateChanged(update)
            }

            Loggly.i(PLAYER, "Loaded $currentSermon at $startingTime")
            subscriber.onComplete()
        } catch (e: Exception) {
            Loggly.e(PLAYER, e, "Could not load the sermon: $pathToSermon")
            subscriber.onError(e)
        }
    }

    fun setVolume(volume: Float) {
        mediaPlayer.setVolume(volume, volume)
        Loggly.v(PLAYER, "Changed volume to $volume")
    }

    // endregion

    // region Player State

    val state: Int
        get() = internalState

    // endregion

    // region MediaPlayer.OnCompletionListener

    override fun onCompletion(player: MediaPlayer?) {
        if (currentSermon == null) return

        callback.onSermonCompleted(currentSermon)
        Loggly.v(PLAYER, "Completed $currentSermon")
    }

    // endregion

    // region Private Methods

    private fun Int.toPlaybackState() = PlaybackStateCompat
            .Builder()
            .setActions(PlaybackStateCompat.ACTION_PAUSE or
                    PlaybackStateCompat.ACTION_PLAY or
                    PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            )
            .setState(
                    this,
                    mediaPlayer.currentPosition.toLong(),
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
        fun onPlayerStateChanged(update: PlayerUpdateModel)
        fun onSermonCompleted(sermonId: String?)
    }

    // endregion
}
