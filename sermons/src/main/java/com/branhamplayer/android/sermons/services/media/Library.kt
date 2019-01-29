package com.branhamplayer.android.sermons.services.media

import android.content.Context
import android.media.MediaMetadataRetriever
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaMetadataCompat
import com.branhamplayer.android.sermons.mappers.DrawerMapper
import com.branhamplayer.android.sermons.models.DrawerItemModel
import com.branhamplayer.android.sermons.repositories.SermonsRepository
import com.branhamplayer.android.services.logging.Loggly
import com.branhamplayer.android.services.logging.LogglyConstants.Tags.SERMON_LIBRARY
import java.util.Stack

class Library(
        context: Context?,
        repository: SermonsRepository = SermonsRepository(),
        drawerListMapper: DrawerMapper = DrawerMapper(context)
) {

    private var currentSermon: DrawerItemModel? = null
    private var currentSermonIndex: Int = 0
    private val rootSermon: DrawerItemModel

    init {
        val sermonsOnDisk = repository.getSermons().value
        rootSermon = drawerListMapper.map(sermonsOnDisk)
    }

    // region Browsing

    fun buildMediaBrowserMenu(parentId: String) = findChildrenByParentId(parentId)
            ?.asSequence()
            ?.map { item ->
                if (item.type == DrawerItemModel.DrawerItemType.FOLDER) {
                    item.toBrowsableMediaItem()
                } else {
                    item.toPlayableMediaItem()
                }
            }?.toMutableList() ?: mutableListOf()

    // endregion

    // region Player Navigation

    val currentOrNull: MediaMetadataCompat?
        get() = currentSermon?.toPlayableMetadata()

    val next: MediaMetadataCompat?
        get() {
            if (currentSermon?.parent == null) return null

            ++currentSermonIndex

            if (currentSermonIndex == currentSermon?.parent?.children?.size) {
                currentSermonIndex = 0
            }

            currentSermon = currentSermon?.parent?.children?.get(currentSermonIndex)
            return currentSermon?.toPlayableMetadata()
        }

    val previous: MediaMetadataCompat?
        get() {
            if (currentSermon?.parent == null) return null

            --currentSermonIndex

            if (currentSermonIndex == -1) {
                currentSermonIndex = currentSermon?.parent?.children?.size?.minus(1) ?: 0
            }

            currentSermon = currentSermon?.parent?.children?.get(currentSermonIndex)
            return currentSermon?.toPlayableMetadata()
        }

    fun setCurrentByMediaId(mediaId: String?) {
        currentSermon = findSermonInTreeByMediaId(mediaId)

        if (currentSermon == null) return
        currentSermonIndex = getIndexOfCurrentSermon()
    }

    // endregion

    // region Private Methods

    private fun findChildrenByParentId(parentId: String): MutableList<DrawerItemModel>? { // DFS
        val stack = Stack<DrawerItemModel>()
        stack.push(rootSermon)

        var currentItem: DrawerItemModel?

        while (!stack.empty()) {
            currentItem = stack.pop()

            if (currentItem?.id == parentId) {
                return currentItem.children
            }

            currentItem.children.forEach { item ->
                stack.add(item)
            }
        }

        return null
    }

    private fun findSermonInTreeByMediaId(mediaId: String?): DrawerItemModel? { // DFS
        val stack = Stack<DrawerItemModel>()
        stack.push(rootSermon)

        var currentItem: DrawerItemModel?

        while (!stack.empty()) {
            currentItem = stack.pop()

            if (currentItem?.id == mediaId) {
                return currentItem
            }

            currentItem.children.forEach { item ->
                stack.add(item)
            }
        }

        return null
    }

    private fun getIndexOfCurrentSermon(): Int {
        currentSermon?.parent?.children?.forEachIndexed { index, sermon ->
            if (sermon.id == currentSermon?.id) {
                return index
            }
        }

        return 0
    }

    // endregion

    // region Extension Functions

    private fun DrawerItemModel.toBrowsableMediaItem(): MediaItem {
        val sermonMetadata = MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, subtitle)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .build()

        return MediaItem(
                sermonMetadata.description,
                MediaItem.FLAG_BROWSABLE
        )
    }

    private fun DrawerItemModel.toPlayableMediaItem() = MediaItem(
            toPlayableMetadata().description,
            MediaItem.FLAG_PLAYABLE
    )

    private fun DrawerItemModel.toPlayableMetadata(): MediaMetadataCompat {
        val audioFileMetadata = MediaMetadataRetriever()
        var durationInMs = 0
        val filePath = id

        try {
            audioFileMetadata.setDataSource(filePath)
            durationInMs = audioFileMetadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toInt()

            Loggly.d(SERMON_LIBRARY, "Found duration of sermon: $filePath, duration: $durationInMs")
        } catch (e: Exception) {
            Loggly.e(SERMON_LIBRARY, e, "Could not determine the duration of $filePath")
        } finally {
            audioFileMetadata.release()
        }

        return MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, subtitle)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, filePath)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, durationInMs.toLong())
                .build()
    }

    // endregion
}
