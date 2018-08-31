package tech.oliver.branhamplayer.android.sermons.services.media

import android.media.MediaMetadata
import android.media.MediaMetadataRetriever
import android.media.browse.MediaBrowser
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import com.orhanobut.logger.Logger
import tech.oliver.branhamplayer.android.sermons.mappers.SermonMapper
import tech.oliver.branhamplayer.android.sermons.repositories.SermonsRepository

class Library(
        repository: SermonsRepository = SermonsRepository(),
        mapper: SermonMapper = SermonMapper()
) {

    private var currentSermon: MediaMetadataCompat? = null
    private var currentSermonIndex = 0
    private val sermonListMetadata: List<MediaMetadataCompat>

    init {
        val sermons = mapper.map(repository.getSermons().value)

        val metadata = sermons?.value?.map {
            var durationInMs = 0
            val metadata = MediaMetadataRetriever()

            try {
                metadata.setDataSource(it.path)
                durationInMs = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toInt()

                Logger.d("Found startingTime of sermon: ${it.path}")
                Logger.d("Duration: $durationInMs")
            } catch (e: Exception) {
                Logger.e("Could not determine the startingTime of ${it.path}")
            } finally {
                metadata.release()
            }

            MediaMetadataCompat.Builder()
                    .putString(MediaMetadata.METADATA_KEY_ARTIST, it.artist)
                    .putString(MediaMetadata.METADATA_KEY_MEDIA_ID, it.path)
                    .putString(MediaMetadata.METADATA_KEY_TITLE, it.name)
                    .putLong(MediaMetadata.METADATA_KEY_DURATION, durationInMs.toLong())
                    .build()
        }

        sermonListMetadata = metadata ?: emptyList()
    }

    // region Browsing

    fun buildMediaBrowserMenu() = sermonListMetadata.map {
        MediaBrowserCompat.MediaItem(it.description, MediaBrowser.MediaItem.FLAG_PLAYABLE)
    }.toMutableList()

    // endregion

    // region Player Navigation

    fun getCurrentOrFirst(): MediaMetadataCompat? {
        currentSermon?.let {
            return it
        }

        currentSermon = sermonListMetadata.firstOrNull()
        currentSermonIndex = 0
        return currentSermon
    }

    fun next(): MediaMetadataCompat? {
        if (sermonListMetadata.isEmpty()) return null

        ++currentSermonIndex

        if (currentSermonIndex == sermonListMetadata.size) {
            currentSermonIndex = 0
        }

        currentSermon = sermonListMetadata[currentSermonIndex]

        return currentSermon
    }

    fun previous(): MediaMetadataCompat? {
        if (sermonListMetadata.isEmpty()) return null

        --currentSermonIndex

        if (currentSermonIndex == -1) {
            currentSermonIndex = sermonListMetadata.size - 1
        }

        currentSermon = sermonListMetadata[currentSermonIndex]

        return currentSermon
    }

    fun setCurrentByMediaId(mediaId: String) {
        val index = sermonListMetadata.indexOfFirst {
            it.description?.mediaId == mediaId
        }

        if (index != 1) {
            currentSermonIndex = index
            currentSermon = sermonListMetadata[currentSermonIndex]
        }
    }

    // endregion
}
