package tech.oliver.branhamplayer.android.sermons

object SermonConstants {
    object Database {
        const val Name = "branham_player"
        const val Version = 1

        object Tables {
            const val Recent = "recent"
            const val Times = "times"
        }
    }

    object Player {
        object CustomActions {
            const val Restart = "tech.oliver.branhamplayer.android.sermons.SermonConstants.Restart"
        }

        object Volume {
            const val FocusGained = 1.0f
            const val FocusLost = 0.2f
        }
    }

    object Notification {
        const val ID = 9001
        const val RequestCode = 424242

        object Actions {
            const val Next = "tech.oliver.branhamplayer.android.sermons.SermonConstants.Next"
            const val Pause = "tech.oliver.branhamplayer.android.sermons.SermonConstants.Pause"
            const val Play = "tech.oliver.branhamplayer.android.sermons.SermonConstants.Play"
            const val Previous = "tech.oliver.branhamplayer.android.sermons.SermonConstants.Previous"
        }

        object Channel {
            const val ID = "tech.oliver.branhamplayer.android.sermons.SermonConstants.Channel"
            const val Name = "Branham Player"
        }
    }

    object Service {
        const val Root = "tech.oliver.branhamplayer.android.sermons.Service"
    }
}
