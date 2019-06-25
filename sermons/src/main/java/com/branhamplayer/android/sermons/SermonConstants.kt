package com.branhamplayer.android.sermons

object SermonConstants {
    object Player {
        object CustomActions {
            const val Restart = "com.branhamplayer.android.sermons.SermonConstants.Restart"
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
            const val Next = "com.branhamplayer.android.sermons.SermonConstants.Next"
            const val Pause = "com.branhamplayer.android.sermons.SermonConstants.Pause"
            const val Play = "com.branhamplayer.android.sermons.SermonConstants.Play"
            const val Previous = "com.branhamplayer.android.sermons.SermonConstants.Previous"
        }

        object Channel {
            const val ID = "com.branhamplayer.android.sermons.SermonConstants.Channel"
            const val Name = "Branham Player"
        }
    }

    object Service {
        const val Root = "com.branhamplayer.android.sermons.Service"
    }
}
