package com.branhamplayer.android.data.mappers

import com.branhamplayer.android.data.DataConstants
import com.branhamplayer.android.data.database.metadata.MetadataEntity
import com.branhamplayer.android.data.models.RawMetadata

class MetadataMapper {

    fun map(rawMetadata: List<RawMetadata>) = rawMetadata.map { metadata ->
        val date = metadata.date?.name

        val location = if (metadata.location?.known == true) {
            "${DataConstants.Mappers.Metadata.divider}${metadata.location.name}"
        } else {
            ""
        }

        val building = if (metadata.building?.known == true) {
            "${DataConstants.Mappers.Metadata.divider}${metadata.building.name}"
        } else {
            ""
        }

        MetadataEntity(
            id = metadata.id ?: "",
            audio = metadata.audio ?: "",
            artworkLarge = metadata.artwork.large ?: "",
            artworkThumbnail = metadata.artwork.thumbnail ?: "",
            title = metadata.title ?: "",
            subtitle = "$date$location$building"
        )
    }
}
