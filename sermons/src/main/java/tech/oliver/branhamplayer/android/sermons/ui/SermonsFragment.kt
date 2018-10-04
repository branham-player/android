package tech.oliver.branhamplayer.android.sermons.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tech.oliver.branhamplayer.android.sermons.R
import tech.oliver.branhamplayer.android.sermons.adapters.SermonsAdapter
import tech.oliver.branhamplayer.android.sermons.utils.permissions.PermissionConstants
import tech.oliver.branhamplayer.android.sermons.utils.permissions.PermissionManager
import tech.oliver.branhamplayer.android.sermons.viewmodels.SermonsViewModel

class SermonsFragment : Fragment() {

    private var sermonAdapter: SermonsAdapter? = null
    private var sermonsRecyclerView: RecyclerView? = null
    private lateinit var viewModel: SermonsViewModel

    private val bg = Schedulers.io()
    private val ui = AndroidSchedulers.mainThread()
    private val compositeDisposable = CompositeDisposable()

    // region Fragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val disposable = PermissionManager(activity)
                .getSinglePermission(PermissionConstants.fileRead)
                .subscribeOn(bg)
                .observeOn(ui)
                .subscribe({ hasPermission ->
                    if (hasPermission) {
                        viewModel = ViewModelProviders.of(this).get(SermonsViewModel::class.java)
                        subscribeUi()
                    } else {
                        showPermissionError()
                    }
                }, {
                    showPermissionError()
                })

        compositeDisposable.add(disposable)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.sermons_fragment, container, false)

        sermonAdapter = SermonsAdapter(context)
        sermonsRecyclerView = view?.findViewById(R.id.sermon_list)
        sermonsRecyclerView?.adapter = sermonAdapter
        sermonsRecyclerView?.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    // endregion

    private fun showPermissionError() = Toast
            .makeText(context, getString(R.string.permission_denied_message), Toast.LENGTH_LONG)
            .show()

    private fun subscribeUi() {
        viewModel.sermons.observe(viewLifecycleOwner, Observer { sermons ->
            sermonAdapter?.setSermons(sermons)
            sermonsRecyclerView?.adapter?.notifyDataSetChanged()
        })
    }

    // region Static Constructor

    companion object {
        fun newInstance() = SermonsFragment()
    }

    // endregion
}
