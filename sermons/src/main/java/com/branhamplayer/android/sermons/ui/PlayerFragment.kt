package com.branhamplayer.android.sermons.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.branhamplayer.android.R as RBase
import com.branhamplayer.android.sermons.R
import com.branhamplayer.android.sermons.di.DaggerInjector
import com.branhamplayer.android.sermons.models.SermonModel
import com.branhamplayer.android.sermons.store.sermonsStore
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.appbar.AppBarLayout
import org.rekotlin.StoreSubscriber
import javax.inject.Inject

class PlayerFragment : Fragment(), StoreSubscriber<SermonModel?> {

    private var unbinder: Unbinder? = null

    // region DI

    @Inject
    lateinit var artworkLoader: RequestManager

    @Inject
    lateinit var backgroundArtworkLoader: RequestManager

    @Inject
    lateinit var crossFade: DrawableTransitionOptions

    // endregion

    // region UI

    @BindView(R.id.player_artwork)
    lateinit var artwork: AppCompatImageView

    @BindView(R.id.player_date)
    lateinit var date: AppCompatTextView

    @BindView(R.id.player_title)
    lateinit var title: AppCompatTextView

    @BindView(R.id.player_toolbar)
    lateinit var toolbar: Toolbar

    // endregion

    // region Fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.player_fragment, container, false)
        unbinder = ButterKnife.bind(this, view)

        context?.let {
            DaggerInjector.buildPlayerComponent(it).inject(this)
        }

        val sermonsActivity = activity as? SermonsActivity?
        sermonsActivity?.setSupportActionBar(toolbar)
        sermonsActivity?.supportActionBar?.setHomeButtonEnabled(true)
        sermonsActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sermonsStore.subscribe(this) {
            it.select { state ->
                state.selectedSermon
            }.skipRepeats()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()

        sermonsStore.unsubscribe(this)
        unbinder?.unbind()
    }

    // endregion

    // region StoreSubscriber

    override fun newState(state: SermonModel?) {
        state?.let {
            date.text = it.formattedDate
            title.text = it.name
            toolbar.title = "" // Don't use the default toolbar, since we extend it with a collapsing toolbar

            // TODO, replace with real album artwork
            artworkLoader
                .load("https://cloudinary-a.akamaihd.net/branham-player/image/upload/c_scale,w_421/samples/landscapes/beach-boat.jpg")
                .timeout(5000)
                .transition(crossFade)
                .error(RBase.drawable.ic_account)
                .placeholder(RBase.drawable.ic_sermons)
                .centerCrop()
                .into(artwork)

//            backgroundArtworkLoader
//                .load("https://cloudinary-a.akamaihd.net/branham-player/image/upload/co_rgb:3086D4,e_colorize:80/e_blur:1600/samples/landscapes/beach-boat.jpg")
//                .timeout(5000)
//                .transition(crossFade)
//                .centerCrop()
//                .into(background)
        }
    }

    // endregion
}
