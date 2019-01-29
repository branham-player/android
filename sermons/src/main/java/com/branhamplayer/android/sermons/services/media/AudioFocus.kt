package com.branhamplayer.android.sermons.services.media

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build

class AudioFocus(
        context: Context,
        private val callback: Callback,
        private val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
) : AudioManager.OnAudioFocusChangeListener {

    private var request: AudioFocusRequest? = null

    // region AudioManager.OnAudioFocusChangeListener

    override fun onAudioFocusChange(change: Int) {
        val mappedChange = when (change) {
            AudioManager.AUDIOFOCUS_GAIN -> ChangeType.PLAY
            AudioManager.AUDIOFOCUS_LOSS -> ChangeType.PAUSE
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> ChangeType.PAUSE
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> ChangeType.DUCK
            else -> ChangeType.PAUSE
        }

        callback.onAudioFocusChanged(mappedChange)
    }

    // endregion

    fun obtain(): Boolean {
        val outcome = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val attributes = AudioAttributes.Builder()
            attributes.setContentType(contentType)
            attributes.setLegacyStreamType(streamType)
            attributes.setUsage(usage)

            val requestBuilder = AudioFocusRequest.Builder(duration)
            requestBuilder.setAcceptsDelayedFocusGain(true)
            requestBuilder.setAudioAttributes(attributes.build())
            requestBuilder.setOnAudioFocusChangeListener(this@AudioFocus)
            requestBuilder.setWillPauseWhenDucked(false)

            val finalRequest = requestBuilder.build()

            request = finalRequest
            audioManager.requestAudioFocus(finalRequest)
        } else {
            // TODO: Figure out how to not need this suppression
            @Suppress("DEPRECATION")
            audioManager.requestAudioFocus(this@AudioFocus, streamType, duration)
        }

        return if (outcome == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            callback.onAudioFocusChanged(ChangeType.PLAY)
            true
        } else {
            false
        }
    }

    fun release() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            request?.let {
                audioManager.abandonAudioFocusRequest(it)
            }
        } else {
            // TODO: Figure out how to not need this suppression
            @Suppress("DEPRECATION")
            audioManager.abandonAudioFocus(this@AudioFocus)
        }
    }

    // region Configuration

    private companion object {
        const val contentType = AudioAttributes.CONTENT_TYPE_MUSIC
        const val duration = AudioManager.AUDIOFOCUS_GAIN
        const val streamType = AudioManager.STREAM_MUSIC
        const val usage = AudioAttributes.USAGE_MEDIA
    }

    // endregion

    // region Callback Definition

    enum class ChangeType {
        DUCK,
        PAUSE,
        PLAY
    }

    interface Callback {
        fun onAudioFocusChanged(change: ChangeType)
    }

    // endregion
}
