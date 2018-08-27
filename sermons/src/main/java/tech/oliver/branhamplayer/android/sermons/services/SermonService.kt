package tech.oliver.branhamplayer.android.sermons.services

import android.media.MediaMetadata
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.media.browse.MediaBrowser
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat
import com.orhanobut.logger.Logger
import tech.oliver.branhamplayer.android.sermons.mappers.SermonMapper
import tech.oliver.branhamplayer.android.sermons.repositories.SermonsRepository
import java.io.IOException

class SermonService : MediaBrowserServiceCompat() {

    private lateinit var player: MediaPlayer
    private lateinit var sermons: List<MediaMetadataCompat>
    private lateinit var session: MediaSessionCompat

    private var currentSermon: MediaMetadataCompat? = null
    private var currentSermonIndex = 0
    private val repository = SermonsRepository()

    override fun onCreate() {
        super.onCreate()

        session = MediaSessionCompat(this, this::class.java.simpleName)
        session.apply {
            setCallback(MediaSessionCallback())
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

            isActive = true
        }

        player = MediaPlayer()
        player.reset()

        sessionToken = session.sessionToken
        sermons = buildSermons()
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

    private fun handlePlay() {
        try {
            player.reset()
            player.setDataSource(currentSermon?.description?.mediaId)

            player.setOnPreparedListener {
                session.setMetadata(currentSermon)
                session.setPlaybackState(buildState(PlaybackStateCompat.STATE_PLAYING))

                it.start()
            }

            player.prepareAsync()
        } catch (e: IOException) {
            Logger.e("Could not load the sermon", e.message)
        }
    }

    override fun onDestroy() {
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

    private fun buildState(state: Int, position: Long = player.currentPosition.toLong()): PlaybackStateCompat {
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
                        position,
                        1f
                )
                .build()
    }

    private inner class MediaSessionCallback : MediaSessionCompat.Callback() {
        override fun onPlay() {
            if (currentSermon == null) {
                currentSermon = sermons.firstOrNull()
                currentSermonIndex = 0
            }

            handlePlay()
        }

        override fun onSeekTo(position: Long) {
            if (currentSermon == null) return

            player.seekTo(position.toInt())
            session.setPlaybackState(buildState(PlaybackStateCompat.STATE_PLAYING, position))
        }

        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
            val index = sermons.indexOfFirst {
                it.description.mediaId == mediaId
            }

            if (index != -1) {
                currentSermon = sermons[index]
                currentSermonIndex = index
            }

            handlePlay()
        }

        override fun onPause() {
            if (!player.isPlaying) return

            player.pause()
            session.setPlaybackState(buildState(PlaybackStateCompat.STATE_PAUSED))
        }

        override fun onSkipToNext() {
            ++currentSermonIndex

            if (currentSermonIndex == sermons.size) {
                currentSermonIndex = 0
            }

            onPlayFromMediaId(sermons[currentSermonIndex].description.mediaId, null)
        }

        override fun onSkipToPrevious() {
            --currentSermonIndex

            if (currentSermonIndex == -1) {
                currentSermonIndex = sermons.size - 1
            }

            onPlayFromMediaId(sermons[currentSermonIndex].description.mediaId, null)
        }
    }
}
