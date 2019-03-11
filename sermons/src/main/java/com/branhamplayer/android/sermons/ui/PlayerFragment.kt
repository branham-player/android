package com.branhamplayer.android.sermons.ui

import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.branhamplayer.android.R as RBase
import com.branhamplayer.android.sermons.R
import com.branhamplayer.android.sermons.models.SermonModel
import com.branhamplayer.android.sermons.store.sermonsStore
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import org.rekotlin.StoreSubscriber

class PlayerFragment : Fragment(), StoreSubscriber<SermonModel?> {

    private var unbinder: Unbinder? = null

    // region UI

    @BindView(R.id.player_artwork)
    lateinit var artwork: AppCompatImageView

    @BindView(R.id.player_background)
    lateinit var background: AppCompatImageView

    @BindView(R.id.player_date)
    lateinit var date: AppCompatTextView

    @BindView(R.id.player_title)
    lateinit var title: AppCompatTextView

    // endregion

    // region Fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.player_fragment, container, false)
        unbinder = ButterKnife.bind(this, view)

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

            // TODO, replace with real album artwork
            Glide.with(this)
                .load("https://cloudinary-a.akamaihd.net/branham-player/image/upload/c_scale,w_421/samples/landscapes/beach-boat.jpg")
                .timeout(5000)
                .transition(DrawableTransitionOptions().crossFade())
                .error(RBase.drawable.ic_account)
                .placeholder(RBase.drawable.ic_sermons)
                .centerCrop()
                .into(artwork)

            Glide.with(this)
                .load("https://cloudinary-a.akamaihd.net/branham-player/image/upload/co_rgb:3086D4,e_colorize:80/e_blur:1600/samples/landscapes/beach-boat.jpg")
                .timeout(5000)
                .transition(DrawableTransitionOptions().crossFade())
                .centerCrop()
                .into(background)
        }
    }

    // endregion
}
