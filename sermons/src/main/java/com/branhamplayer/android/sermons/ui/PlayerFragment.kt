package com.branhamplayer.android.sermons.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.branhamplayer.android.sermons.R
import com.branhamplayer.android.sermons.states.SermonsState
import com.branhamplayer.android.sermons.store.sermonsStore
import org.rekotlin.StoreSubscriber

class PlayerFragment : Fragment(), StoreSubscriber<SermonsState> {

    private var unbinder: Unbinder? = null

    // region UI

    @BindView(R.id.player_button)
    lateinit var button: AppCompatButton

    // endregion

    // region Fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.player_fragment, container, false)

        unbinder = ButterKnife.bind(this, view)
        sermonsStore.subscribe(this)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()

        sermonsStore.unsubscribe(this)
        unbinder?.unbind()
    }

    // endregion

    // region StoreSubscriber

    override fun newState(state: SermonsState) {
        state.selectedSermon?.let {
            button.text = it.name
        }
    }

    // endregion
}
