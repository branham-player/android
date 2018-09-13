package tech.oliver.branhamplayer.android.sermons.services.media

import android.media.MediaMetadataRetriever
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import tech.oliver.branhamplayer.android.sermons.mappers.SermonMapper
import tech.oliver.branhamplayer.android.sermons.repositories.SermonsRepository
import tech.oliver.branhamplayer.android.sermons.services.logging.Loggly
import tech.oliver.branhamplayer.android.sermons.services.logging.LogglyConstants.Tags.SERMON_LIBRARY

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

                Loggly.d(SERMON_LIBRARY,"Found starting time of sermon: ${it.path}, duration: $durationInMs")
            } catch (e: Exception) {
                Loggly.e(SERMON_LIBRARY, e, "Could not determine the startingTime of ${it.path}")
            } finally {
                metadata.release()
            }

            MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, it.formattedDate)
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, it.path)
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, it.name)
                    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, durationInMs.toLong())
                    .build()
        }

        sermonListMetadata = metadata ?: emptyList()
    }

    // region Browsing

    fun buildMediaBrowserMenu() = sermonListMetadata.map {
        MediaBrowserCompat.MediaItem(it.description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
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
