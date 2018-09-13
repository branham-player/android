package tech.oliver.branhamplayer.android.sermons.services

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tech.oliver.branhamplayer.android.sermons.SermonConstants
import tech.oliver.branhamplayer.android.sermons.services.media.AudioFocus
import tech.oliver.branhamplayer.android.sermons.services.media.Library
import tech.oliver.branhamplayer.android.sermons.services.media.Player
import tech.oliver.branhamplayer.android.sermons.viewmodels.PlayerViewModel

class SermonService : MediaBrowserServiceCompat(), AudioFocus.Callback, Player.Callback {

    private lateinit var focus: AudioFocus
    private lateinit var library: Library
    private lateinit var notification: SermonNotification
    private lateinit var player: Player
    private lateinit var session: MediaSessionCompat
    private lateinit var viewModel: PlayerViewModel

    private val bg: Scheduler = Schedulers.io()
    private val ui: Scheduler = AndroidSchedulers.mainThread()
    private val disposable: CompositeDisposable = CompositeDisposable()

    // region MediaBrowserServiceCompat

    override fun onCreate() {
        super.onCreate()

        val callback = MediaSessionCallback()

        initDependencies(callback)
        initSession(callback)
        restoreFromLastSession()
    }

    override fun onDestroy() {
        session.isActive = false

        disposable.dispose()
        player.release()
        session.release()
        viewModel.release()
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?) =
            BrowserRoot(SermonConstants.Service.Root, null)

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaItem>>) {
        result.sendResult(library.buildMediaBrowserMenu())
    }

    // endregion

    // region AudioFocus.Callback

    override fun onAudioFocusChanged(change: AudioFocus.ChangeType) = when (change) {
        AudioFocus.ChangeType.DUCK -> {
            player.setVolume(SermonConstants.Player.Volume.FocusLost)
        }

        AudioFocus.ChangeType.PAUSE -> {
            player.pause()
            player.setVolume(SermonConstants.Player.Volume.FocusGained)
        }

        AudioFocus.ChangeType.PLAY -> {
            player.resumeCurrent()
            player.setVolume(SermonConstants.Player.Volume.FocusGained)
        }
    }

    // endregion

    // region Player.Callback

    override fun onPlayerStateChanged(state: PlaybackStateCompat, mediaId: String?, duration: Int) = when (state.state) {
        PlaybackStateCompat.STATE_PAUSED -> {
            val sermon = library.getCurrentOrFirst()

            focus.release()
            notification.update(sermon, state, sessionToken)
            session.setMetadata(sermon)
            session.setPlaybackState(state)
            viewModel.savePausedDuration(mediaId, duration)
        }

        PlaybackStateCompat.STATE_PLAYING -> {
            val sermon = library.getCurrentOrFirst()

            focus.obtain()
            notification.update(sermon, state, sessionToken)
            session.setMetadata(sermon)
            session.setPlaybackState(state)
            viewModel.saveMostRecentSermon(mediaId)
        }

        else -> Unit
    }

    override fun onSermonCompleted(mediaId: String?) {
        viewModel.savePausedDuration(mediaId, 0)
        player.setCurrentWithoutPlaying(mediaId, 0)
    }

    // endregion

    // region Private Methods

    private fun initDependencies(callback: MediaSessionCallback) {
        focus = AudioFocus(applicationContext, this)
        library = Library()
        notification = SermonNotification(this, callback)
        player = Player(applicationContext, this)
        viewModel = PlayerViewModel(applicationContext)
    }

    private fun initSession(callback: MediaSessionCallback) {
        session = MediaSessionCompat(applicationContext, this::class.java.simpleName)
        session.isActive = true
        session.setCallback(callback)
        session.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)

        sessionToken = session.sessionToken
    }

    private fun restoreFromLastSession() {
        var mediaId = ""
        val subscription = viewModel.getMostRecentSermonAsync()
                .flatMap {
                    mediaId = it
                    viewModel.getPausedDurationAsync(mediaId)
                }.flatMap { startingTime ->
                    player.setCurrentWithoutPlaying(mediaId, startingTime)
                }
                .subscribeOn(bg)
                .observeOn(ui)
                .subscribe({}, {})

        disposable.add(subscription)
    }

    // endregion

    inner class MediaSessionCallback : MediaSessionCompat.Callback() {

        override fun onPlay() = onPlayFromMediaId(library.getCurrentOrFirst()?.id, null)
        override fun onPause() = player.pause()
        override fun onSkipToNext() = onPlayFromMediaId(library.next()?.id, null)
        override fun onSkipToPrevious() = onPlayFromMediaId(library.previous()?.id, null)

        override fun onCustomAction(action: String?, extras: Bundle?) = when (action) {
            SermonConstants.Player.CustomActions.Restart -> player.restartCurrentFromBeginning()
            else -> Unit
        }

        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
            player.pause()

            val subscription = viewModel.getPausedDurationAsync(mediaId)
                    .subscribeOn(bg)
                    .observeOn(ui)
                    .subscribe({ startingTime ->
                        player.play(mediaId, startingTime)
                    }, {
                        player.play(mediaId, 0)
                    })

            disposable.add(subscription)
        }

        private val MediaMetadataCompat.id: String?
            get() = this.description.mediaId
    }
}
