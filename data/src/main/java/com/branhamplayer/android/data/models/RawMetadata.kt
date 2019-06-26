package com.branhamplayer.android.data.models

data class RawMetadata(
    val id: String?,
    val audio: String?,
    val date: MetadataDate?,
    val location: MetadataLocation?,
    val title: String?,
    val building: MetadataBuilding?,
    val artwork: MetadataArtwork
) {

    data class MetadataDate(
        val known: Boolean?,
        val name: String?
    )

    data class MetadataLocation(
        val known: Boolean?,
        val name: String?
    )

    data class MetadataBuilding(
        val known: Boolean?,
        val name: String?
    )

    data class MetadataArtwork(
        val large: String?,
        val thumbnail: String?
    )
}
