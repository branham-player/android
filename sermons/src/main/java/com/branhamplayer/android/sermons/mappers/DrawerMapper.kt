package com.branhamplayer.android.sermons.mappers

import android.content.Context
import com.branhamplayer.android.sermons.R
import com.branhamplayer.android.sermons.models.DrawerItemModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Stack
import java.util.UUID

class DrawerMapper(
        private val context: Context?,
        private val sermonListMapper: SermonListMapper = SermonListMapper()
) {

    fun map(input: List<File>?): DrawerItemModel {

        val root = DrawerItemModel(
                id = "com.branhamplayer.android.sermons.Service",
                title = "",
                type = DrawerItemModel.DrawerItemType.FOLDER
        )

        if (input == null) return root
        val mappedSermons = sermonListMapper.map(input)?.value ?: return root

        mappedSermons.asSequence().sortedBy { sermon ->
            sermon.date
        }.forEach { sermon ->

            val month = SimpleDateFormat("MMMM", Locale.US).format(sermon.date)
            val year = SimpleDateFormat("YYYY", Locale.US).format(sermon.date)
            val decade = (Math.floor(year.toInt() / 10.0) * 10).toInt()

            val formattedDecade = context?.getString(R.string.drawer_decade, decade)
                    ?: decade.toString()
            val formattedMonth = "$month $year"

            val levels = listOf(
                    formattedDecade, // 1950s
                    year, // 1956
                    formattedMonth // May 1956
            )

            val mappedSermon = DrawerItemModel(
                    id = sermon.path,
                    subtitle = sermon.formattedDate,
                    title = sermon.name,
                    type = DrawerItemModel.DrawerItemType.SERMON
            )

            insertSermonWithAllLevels(root, levels, mappedSermon)
        }

        updateAllFolderSubtitlesWithCount(root)

        return root
    }

    // region Private Methods

    private fun insertSermonWithAllLevels(root: DrawerItemModel, levels: List<String>, sermon: DrawerItemModel) {
        var parent = root

        levels.forEach { currentLevel ->
            parent = if (!parent.hasChild(currentLevel)) {
                val insertedLevel = DrawerItemModel(
                        id = randomGuid(),
                        parent = parent,
                        title = currentLevel,
                        type = DrawerItemModel.DrawerItemType.FOLDER
                )

                parent.children.add(insertedLevel)
                insertedLevel
            } else {
                parent.getChild(currentLevel)
            }

            ++parent.sermonCount
        }

        sermon.parent = parent
        parent.children.add(sermon)
    }

    private fun randomGuid() = UUID.randomUUID().toString()

    private fun updateAllFolderSubtitlesWithCount(root: DrawerItemModel) { // DFS
        val stack = Stack<DrawerItemModel>()
        stack.push(root)

        var currentItem: DrawerItemModel?

        while (!stack.empty()) {
            currentItem = stack.pop()

            if (currentItem?.type == DrawerItemModel.DrawerItemType.FOLDER) {
                currentItem.subtitle = if (currentItem.sermonCount == 1) {
                    context?.getString(R.string.drawer_sermon, currentItem.sermonCount) ?: currentItem.sermonCount.toString()
                } else {
                    context?.getString(R.string.drawer_sermons, currentItem.sermonCount) ?: currentItem.sermonCount.toString()
                }
            }

            currentItem.children.forEach { item ->
                stack.add(item)
            }
        }
    }

    // endregion

    // region Extension Functions

    private fun DrawerItemModel.hasChild(title: String) = children.any { item ->
        item.title == title
    }

    private fun DrawerItemModel.getChild(title: String) = children.first { item ->
        item.title == title
    }

    // endregion
}
