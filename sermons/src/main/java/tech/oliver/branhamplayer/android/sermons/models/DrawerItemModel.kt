package tech.oliver.branhamplayer.android.sermons.models

data class DrawerItemModel(
        val children: MutableList<DrawerItemModel> = mutableListOf(),
        var parent: DrawerItemModel? = null,

        val id: String,
        var sermonCount: Int = 0,
        var subtitle: String = "",
        val title: String,

        val type: DrawerItemType
) {

    enum class DrawerItemType {
        FOLDER,
        SERMON
    }
}
