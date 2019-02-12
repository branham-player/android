package com.branhamplayer.android.sermons.shared

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.branhamplayer.android.sermons.adapters.SermonsAdapter
import com.branhamplayer.android.sermons.mappers.SermonMapper
import com.branhamplayer.android.sermons.repositories.SermonsRepository
import com.branhamplayer.android.sermons.ui.SermonListFragment
import com.branhamplayer.android.services.auth0.Auth0Service
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.dsl.module.module

@SuppressLint("ShowToast")
val activityManagementModule = module {

    factory(override = true) {
        SermonListFragment()
    }

    factory(override = true) { (context: Context, @StringRes message: Int, duration: Int) ->
        Toast.makeText(context, context.getString(message), duration)
    }
}

val auth0Module = module {

    factory(override = true) {
        Auth0Service()
    }
}

val dataModule = module {

    single(override = true) {
        SermonMapper()
    }

    single(override = true) {
        SermonsRepository()
    }
}

val recyclerViewModule = module {

    factory(override = true) { (context: Context?) ->
        LinearLayoutManager(context)
    }

    factory(override = true) { (context: Context?) ->
        SermonsAdapter(context)
    }
}

val rxJavaModule = module {

    factory(name = SermonsModuleConstants.UI_THREAD, override = true) {
        AndroidSchedulers.mainThread()
    }

    factory(name = SermonsModuleConstants.BG_THREAD, override = true) {
        Schedulers.io()
    }

    factory(override = true) {
        CompositeDisposable()
    }
}

val sermonsModule = listOf(activityManagementModule, auth0Module, dataModule, recyclerViewModule, rxJavaModule)
