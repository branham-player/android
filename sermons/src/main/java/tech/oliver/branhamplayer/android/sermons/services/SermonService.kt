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
import tech.oliver.branhamplayer.android.sermons.models.PlayerUpdateModel
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

    override fun onAudioFocusChanged(change: AudioFocus.ChangeType) {
        when (change) {
            AudioFocus.ChangeType.DUCK -> {
                player.setVolume(SermonConstants.Player.Volume.FocusLost)
            }

            AudioFocus.ChangeType.PAUSE -> {
                player.pause()
                player.setVolume(SermonConstants.Player.Volume.FocusGained)
            }

            AudioFocus.ChangeType.PLAY -> {
                player.resumeCurrent()
                        .andThen {
                            player.setVolume(SermonConstants.Player.Volume.FocusGained)
                        }
                        .subscribeOn(bg)
                        .observeOn(ui)
                        .subscribe({}, {})
            }
        }
    }

    // endregion

    // region Player.Callback


    override fun onPlayerStateChanged(update: PlayerUpdateModel) {

        library.setCurrentByMediaId(update.newSermon.id)
        val sermon = library.currentOrFirst
        val state = update.state

        notification.update(sermon, state, sessionToken)
        session.setMetadata(sermon)
        session.setPlaybackState(state)
        viewModel.saveMostRecentSermon(update.newSermon.id)

        if (update.oldSermon.id != null) {
            viewModel.savePausedDuration(update.oldSermon.id, update.oldSermon.duration)
        }

        when (state.state) {
            PlaybackStateCompat.STATE_PAUSED -> {
                focus.release()
            }

            PlaybackStateCompat.STATE_PLAYING -> {
                focus.obtain()
            }

            else -> Unit
        }
    }

    override fun onSermonCompleted(sermonId: String?) {
        viewModel.savePausedDuration(sermonId, 0)
        player.setCurrentWithoutPlaying(sermonId, 0)
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
        var sermonId = ""
        val subscription = viewModel.getMostRecentSermonAsync()
                .flatMap {
                    sermonId = it
                    viewModel.getPausedDurationAsync(sermonId)
                }.flatMapCompletable { startingTime ->
                    player.setCurrentWithoutPlaying(sermonId, startingTime)
                }
                .subscribeOn(bg)
                .observeOn(ui)
                .subscribe({}, {})

        disposable.add(subscription)
    }

    // endregion

    inner class MediaSessionCallback : MediaSessionCompat.Callback() {

        override fun onPlay() = loadSermon(library.currentOrFirst?.id, true)
        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) = loadSermon(mediaId, true)
        override fun onPause() = player.pause()
        override fun onSkipToNext() = loadSermon(library.next?.id)
        override fun onSkipToPrevious() = loadSermon(library.previous?.id)

        override fun onCustomAction(action: String?, extras: Bundle?) {
            when (action) {
                SermonConstants.Player.CustomActions.Restart -> {
                    val subscription = player.restartCurrentFromBeginning()
                            .subscribeOn(bg)
                            .observeOn(ui)
                            .subscribe({}, {})

                    disposable.add(subscription)
                }

                else -> Unit
            }
        }

        private fun loadSermon(sermonId: String?, forcePlay: Boolean = false) {
            val shouldPlayWhenReady = forcePlay || player.state == PlaybackStateCompat.STATE_PLAYING

            val subscription = viewModel.getPausedDurationAsync(sermonId)
                    .flatMapCompletable { startingTime ->
                        if (shouldPlayWhenReady) {
                            player.pause(false)
                            player.play(sermonId, startingTime)
                        } else {
                            player.setCurrentWithoutPlaying(sermonId, startingTime)
                        }
                    }
                    .subscribeOn(bg)
                    .observeOn(ui)
                    .subscribe({}, {})

            disposable.add(subscription)
        }

        private val MediaMetadataCompat.id: String?
            get() = this.description.mediaId
    }
}
