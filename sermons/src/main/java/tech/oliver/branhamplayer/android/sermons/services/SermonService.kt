package tech.oliver.branhamplayer.android.sermons.services

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaMetadata
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.media.browse.MediaBrowser
import android.os.Bundle
import android.os.PowerManager
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat
import com.orhanobut.logger.Logger
import tech.oliver.branhamplayer.android.sermons.R
import tech.oliver.branhamplayer.android.sermons.SermonConstants
import tech.oliver.branhamplayer.android.sermons.mappers.SermonMapper
import tech.oliver.branhamplayer.android.sermons.repositories.SermonsRepository
import tech.oliver.branhamplayer.android.sermons.viewmodels.SermonServiceViewModel
import java.io.IOException

class SermonService : MediaBrowserServiceCompat(), AudioManager.OnAudioFocusChangeListener {

    private lateinit var audioManager: AudioManager
    private lateinit var player: MediaPlayer
    private lateinit var notification: SermonNotification
    private lateinit var sermons: List<MediaMetadataCompat>
    private lateinit var session: MediaSessionCompat

    private var currentSermon: MediaMetadataCompat? = null
    private var currentSermonIndex = 0
    private val repository = SermonsRepository()

    private lateinit var viewModel: SermonServiceViewModel

    override fun onCreate() {
        super.onCreate()

        val callback = MediaSessionCallback()

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        notification = SermonNotification(this, callback)
        viewModel = SermonServiceViewModel(applicationContext)

        session = MediaSessionCompat(this, this::class.java.simpleName)
        session.apply {
            setCallback(callback)
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

            isActive = true
        }

        player = MediaPlayer()

        player.apply {
            val attributes = AudioAttributes.Builder().apply {
                setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                setLegacyStreamType(AudioManager.STREAM_MUSIC)
                setUsage(AudioAttributes.USAGE_MEDIA)
            }

            reset()
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
            //setAudioAttributes(attributes.build())
        }

        sessionToken = session.sessionToken
        sermons = buildSermons()

        viewModel.getMostRecentSermonAsync { sermon ->
            sermon?.let {
                val bundle = Bundle()
                bundle.putString("shouldPlay", "false")

                callback.onPlayFromMediaId(it, bundle)
            }
        }
    }

    private fun buildSermons(): List<MediaMetadataCompat> {
        val sermons = SermonMapper().map(repository.getSermons().value)
        val mediaEntries = sermons?.value?.map {
            var durationInMs = 0L
            val metadata = MediaMetadataRetriever()

            try {
                metadata.setDataSource(it.path)
                durationInMs = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()
            } catch (e: Exception) {

            } finally {
                metadata.release()
            }

            MediaMetadataCompat.Builder()
                    .putString(MediaMetadata.METADATA_KEY_MEDIA_ID, it.path)
                    .putString(MediaMetadata.METADATA_KEY_TITLE, it.name)
                    .putString(MediaMetadata.METADATA_KEY_ARTIST, it.artist)
                    .putLong(MediaMetadata.METADATA_KEY_DURATION, durationInMs)
                    .build()
        }

        return mediaEntries ?: emptyList()
    }

    override fun onDestroy() {
        saveMostRecent()
        saveTime()

        player.release()
        session.release()
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?) =
            BrowserRoot(this::class.java.simpleName, null)

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaItem>>) {
        val sermonListList = sermons.map {
            MediaItem(it.description, MediaBrowser.MediaItem.FLAG_PLAYABLE)
        }

        result.sendResult(sermonListList.toMutableList())
    }

    override fun onAudioFocusChange(focusChange: Int) {
        player.setVolume(1.0f, 1.0f)
    }

    private fun buildState(state: Int, position: Int = player.currentPosition): PlaybackStateCompat {
        return PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY or
                        PlaybackStateCompat.ACTION_PLAY_PAUSE or
                        PlaybackStateCompat.ACTION_PAUSE or
                        PlaybackStateCompat.ACTION_SEEK_TO or
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                        PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
                )
                .setState(
                        state,
                        position.toLong(),
                        1f
                )
                .addCustomAction(
                        PlaybackStateCompat.CustomAction.Builder(
                                SermonConstants.Player.CustomActions.Restart,
                                getString(R.string.player_restart),
                                R.drawable.restart
                        ).build()
                )
                .build()
    }

    private fun saveMostRecent() {
        viewModel.saveMostRecentSermon(currentSermon?.description?.mediaId)
    }

    private fun saveTime() {
        viewModel.savePausedDuration(
                currentSermon?.description?.mediaId,
                player.currentPosition
        )
    }

    inner class MediaSessionCallback : MediaSessionCompat.Callback() {
        override fun onPlay() {
            if (currentSermon == null) {
                currentSermon = sermons.firstOrNull()
                currentSermonIndex = 0
            }

            preparePlayer()
        }

        override fun onSeekTo(position: Long) {
            if (currentSermon == null) return

            val state = buildState(PlaybackStateCompat.STATE_PAUSED)

            player.seekTo(position.toInt())
            notification.update(currentSermon, state, sessionToken)
            session.setPlaybackState(state)
        }

        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
            val index = sermons.indexOfFirst {
                it.description.mediaId == mediaId
            }

            if (index != -1) {
                currentSermon = sermons[index]
                currentSermonIndex = index
            }

            var shouldPlay = true

            if (extras?.getString("shouldPlay") == "false") {
                shouldPlay = false
            }

            preparePlayer(shouldPlay)
        }

        override fun onPause() {
            if (!player.isPlaying) return

            val state = buildState(PlaybackStateCompat.STATE_PAUSED)

            saveTime()
            saveMostRecent()
            player.pause()
            notification.update(currentSermon, state, sessionToken)
            session.setPlaybackState(state)
            audioManager.abandonAudioFocus(this@SermonService)
        }

        override fun onSkipToNext() {
            saveTime()

            ++currentSermonIndex

            if (currentSermonIndex == sermons.size) {
                currentSermonIndex = 0
            }

            onPlayFromMediaId(sermons[currentSermonIndex].description.mediaId, null)
        }

        override fun onSkipToPrevious() {
            saveTime()

            --currentSermonIndex

            if (currentSermonIndex == -1) {
                currentSermonIndex = sermons.size - 1
            }

            onPlayFromMediaId(sermons[currentSermonIndex].description.mediaId, null)
        }

        override fun onCustomAction(action: String?, extras: Bundle?) {
            when (action) {
                SermonConstants.Player.CustomActions.Restart -> restart()
            }
        }

        fun restart() {
            player.pause()
            player.seekTo(0)

            saveTime()
            saveMostRecent()
            onPlay()
        }

        private fun preparePlayer(shouldPlay: Boolean = true) {
            try {
                player.reset()
                player.setDataSource(currentSermon?.description?.mediaId)
                saveMostRecent()

                viewModel.getPausedDurationAsync(currentSermon?.description?.mediaId) { duration ->
                    player.setOnPreparedListener { readyPlayer ->
                        val state = when (shouldPlay) {
                            true -> buildState(PlaybackStateCompat.STATE_PLAYING, duration)
                            false -> buildState(PlaybackStateCompat.STATE_PAUSED, duration)
                        }

                        audioManager.requestAudioFocus(this@SermonService, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
                        notification.update(currentSermon, state, sessionToken)
                        session.setMetadata(currentSermon)
                        session.setPlaybackState(state)

                        readyPlayer.seekTo(duration)

                        if (shouldPlay) {
                            readyPlayer.start()
                        }
                    }

                    player.prepareAsync()
                }
            } catch (e: IOException) {
                Logger.e("Could not load the sermon", e.message)
            }
        }
    }
}
