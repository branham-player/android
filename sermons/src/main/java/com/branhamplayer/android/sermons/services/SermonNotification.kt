package com.branhamplayer.android.sermons.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.branhamplayer.android.R as RBase
import com.branhamplayer.android.sermons.R
import com.branhamplayer.android.sermons.SermonConstants
import com.branhamplayer.android.sermons.ui.SermonsActivity
import com.branhamplayer.android.services.logging.Loggly
import com.branhamplayer.android.services.logging.LogglyConstants.Tags.NOTIFICATION

class SermonNotification(
        private val service: SermonService,
        private val callback: SermonService.MediaSessionCallback
) : BroadcastReceiver() {

    private val notificationManager: NotificationManager
    private var started = false

    private val nextAction: NotificationCompat.Action
    private val pauseAction: NotificationCompat.Action
    private val playAction: NotificationCompat.Action
    private val previousAction: NotificationCompat.Action

    init {
        val nextIntent = PendingIntent.getBroadcast(
                service.applicationContext,
                SermonConstants.Notification.RequestCode,
                Intent(SermonConstants.Notification.Actions.Next).setPackage(service.packageName),
                PendingIntent.FLAG_CANCEL_CURRENT
        )

        val pauseIntent = PendingIntent.getBroadcast(
                service.applicationContext,
                SermonConstants.Notification.RequestCode,
                Intent(SermonConstants.Notification.Actions.Pause).setPackage(service.packageName),
                PendingIntent.FLAG_CANCEL_CURRENT
        )

        val playIntent = PendingIntent.getBroadcast(
                service.applicationContext,
                SermonConstants.Notification.RequestCode,
                Intent(SermonConstants.Notification.Actions.Play).setPackage(service.packageName),
                PendingIntent.FLAG_CANCEL_CURRENT
        )

        val previousIntent = PendingIntent.getBroadcast(
                service.applicationContext,
                SermonConstants.Notification.RequestCode,
                Intent(SermonConstants.Notification.Actions.Previous).setPackage(service.packageName),
                PendingIntent.FLAG_CANCEL_CURRENT
        )

        nextAction = NotificationCompat.Action(
                android.R.drawable.ic_media_next,
                service.getString(R.string.notification_next),
                nextIntent
        )

        pauseAction = NotificationCompat.Action(
                android.R.drawable.ic_media_pause,
                service.getString(R.string.notification_pause),
                pauseIntent
        )

        playAction = NotificationCompat.Action(
                android.R.drawable.ic_media_play,
                service.getString(R.string.notification_play),
                playIntent
        )

        previousAction = NotificationCompat.Action(
                android.R.drawable.ic_media_previous,
                service.getString(R.string.notification_previous),
                previousIntent
        )

        val intentFilter = IntentFilter()
        intentFilter.addAction(SermonConstants.Notification.Actions.Next)
        intentFilter.addAction(SermonConstants.Notification.Actions.Pause)
        intentFilter.addAction(SermonConstants.Notification.Actions.Play)
        intentFilter.addAction(SermonConstants.Notification.Actions.Previous)

        service.registerReceiver(this, intentFilter)

        notificationManager = service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    SermonConstants.Notification.Channel.ID,
                    SermonConstants.Notification.Channel.Name,
                    NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.apply {
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
                setShowBadge(true)
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            SermonConstants.Notification.Actions.Next -> callback.onSkipToNext()
            SermonConstants.Notification.Actions.Pause -> callback.onPause()
            SermonConstants.Notification.Actions.Play -> callback.onPlay()
            SermonConstants.Notification.Actions.Previous -> callback.onSkipToPrevious()
        }
    }

    fun update(metadata: MediaMetadataCompat?, playback: PlaybackStateCompat?, token: MediaSessionCompat.Token?) {
        if (playback?.state == PlaybackStateCompat.STATE_STOPPED || playback?.state == PlaybackStateCompat.STATE_NONE) {
            service.stopForeground(true)

            try {
                service.unregisterReceiver(this)
            } catch (e: Exception) {
                Loggly.e(NOTIFICATION, e, "The notification manager receiver is not registered")
            } finally {
                service.stopSelf()
            }

            return
        }

        if (metadata == null) {
            return
        }

        val description = metadata.description
        val isPlaying = playback?.state == PlaybackStateCompat.STATE_PLAYING

        val notificationBuilder = NotificationCompat.Builder(service, SermonConstants.Notification.Channel.ID)

        notificationBuilder.apply {
            val playOrPause = if (isPlaying) {
                pauseAction
            } else {
                playAction
            }

            val style = with(androidx.media.app.NotificationCompat.MediaStyle()) {
                setMediaSession(token)
                setShowActionsInCompactView(0, 1, 2) // Adds previous, play/pause, next
            }

            val time = if (isPlaying && playback != null) {
                System.currentTimeMillis() - playback.position
            } else {
                0L
            }

            color = ContextCompat.getColor(service.applicationContext, RBase.color.colorPrimary)

            setChannelId(SermonConstants.Notification.Channel.ID)
            setContentIntent(createContentIntent())
            setContentText(description.subtitle)
            setContentTitle(description.title)
            setOngoing(isPlaying)
            setShowWhen(isPlaying)
            setSmallIcon(android.R.drawable.ic_media_play)
            setStyle(style)
            setUsesChronometer(isPlaying)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setWhen(time)

            // Add in the previous button, if enabled
            if (playback?.actions?.and(PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) ?: 0L != 0L) {
                addAction(previousAction)
            }

            // Add play/pause
            addAction(playOrPause)

            // Add in the next button, if enabled
            if (playback?.actions?.and(PlaybackStateCompat.ACTION_SKIP_TO_NEXT) ?: 0L != 0L) {
                addAction(nextAction)
            }
        }

        val notification = notificationBuilder.build()

        if (isPlaying && !started) {
            service.startService(Intent(service.applicationContext, SermonService::class.java))
            service.startForeground(SermonConstants.Notification.ID, notification)

            started = true
        } else {
            if (!isPlaying) {
                service.stopForeground(false)
                started = false
            }

            notificationManager.notify(SermonConstants.Notification.ID, notification)
        }
    }

    private fun createContentIntent(): PendingIntent {
        val openApp = Intent(service.applicationContext, SermonsActivity::class.java)
        openApp.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        return PendingIntent.getActivity(
                service.applicationContext,
                SermonConstants.Notification.RequestCode,
                openApp,
                PendingIntent.FLAG_CANCEL_CURRENT
        )
    }
}
