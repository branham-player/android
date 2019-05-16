package com.branhamplayer.android.data

object DataConstants {
    object Database {
        const val name = "branham_player"
        const val version = 1

        object Tables {
            const val metadata = "metadata"
            const val versions = "versions"

            object Metadata {
                const val autoGenerateId = false

                const val id = "id"
            }

            object Versions {
                const val autoGenerateId = true

                const val property = "property"
                const val version = "version"

                const val metadataVersion = "metadata_version"
            }
        }
    }

    object Network {
        const val rawMetadata = "https://raw.githubusercontent.com/branham-player/golden-dataset"
    }
}
