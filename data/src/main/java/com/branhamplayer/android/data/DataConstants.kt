package com.branhamplayer.android.data

object DataConstants {
    object Database {
        const val name = "branham_player"
        const val version = 1

        object Tables {
            const val metadata = "metadata"
            const val versions = "versions"

            object Metadata {
                const val metadataVersion = "metadata_version"
            }
        }
    }

    object Mappers {
        object Metadata {
            const val divider = " - "
        }
    }

    object Network {
        object RawMetadata {
            const val baseUrl = "https://raw.githubusercontent.com/branham-player/golden-dataset/"
            const val path = "{version}/condensed.json"
        }
    }
}
