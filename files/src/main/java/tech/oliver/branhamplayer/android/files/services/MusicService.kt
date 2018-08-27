package tech.oliver.branhamplayer.android.files.services

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
import tech.oliver.branhamplayer.android.files.mappers.FileListMapper
import tech.oliver.branhamplayer.android.files.repositories.FileListRepository
import java.io.IOException

class MusicService : MediaBrowserServiceCompat() {

    private lateinit var player: MediaPlayer
    private lateinit var songs: List<MediaMetadataCompat>
    private lateinit var session: MediaSessionCompat

    private var currentSong: MediaMetadataCompat? = null
    private var currentSongIdx = 0
    private val repository = FileListRepository()

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
        songs = buildSongs()
    }

    private fun buildSongs(): List<MediaMetadataCompat> {
        val songs = FileListMapper().map(repository.getFiles().value)
        val mediaEntries = songs?.value?.map {
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
            player.setDataSource(currentSong?.description?.mediaId)

            player.setOnPreparedListener {
                session.setMetadata(currentSong)
                session.setPlaybackState(buildState(PlaybackStateCompat.STATE_PLAYING))

                it.start()
            }

            player.prepareAsync()
        } catch (e: IOException) {
            Logger.e("Could not load the song", e.message)
        }
    }

    override fun onDestroy() {
        player.release()
        session.release()
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?) =
            BrowserRoot(this::class.java.simpleName, null)

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaItem>>) {
        val songList = songs.map {
            MediaItem(it.description, MediaBrowser.MediaItem.FLAG_PLAYABLE)
        }

        result.sendResult(songList.toMutableList())
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
            if (currentSong == null) {
                currentSong = songs.firstOrNull()
                currentSongIdx = 0
            }

            handlePlay()
        }

        override fun onSeekTo(position: Long) {
            if (currentSong == null) return

            player.seekTo(position.toInt())
            session.setPlaybackState(buildState(PlaybackStateCompat.STATE_PLAYING, position))
        }

        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
            val index = songs.indexOfFirst {
                it.description.mediaId == mediaId
            }

            if (index != -1) {
                currentSong = songs[index]
                currentSongIdx = index
            }

            handlePlay()
        }

        override fun onPause() {
            if (!player.isPlaying) return

            player.pause()
            session.setPlaybackState(buildState(PlaybackStateCompat.STATE_PAUSED))
        }

        override fun onSkipToNext() {
            ++currentSongIdx

            if (currentSongIdx == songs.size) {
                currentSongIdx = 0
            }

            onPlayFromMediaId(songs[currentSongIdx].description.mediaId, null)
        }

        override fun onSkipToPrevious() {
            --currentSongIdx

            if (currentSongIdx == -1) {
                currentSongIdx = songs.size - 1
            }

            onPlayFromMediaId(songs[currentSongIdx].description.mediaId, null)
        }
    }
}
