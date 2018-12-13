package tech.oliver.branhamplayer.android.sermons.shared

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.RouterTransaction
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.dsl.module.module
import tech.oliver.branhamplayer.android.sermons.SermonsActivity
import tech.oliver.branhamplayer.android.sermons.SermonsActivityImpl
import tech.oliver.branhamplayer.android.sermons.adapters.SermonsAdapter
import tech.oliver.branhamplayer.android.sermons.controllers.SermonsController
import tech.oliver.branhamplayer.android.sermons.mappers.SermonMapper
import tech.oliver.branhamplayer.android.sermons.repositories.SermonsRepository

@SuppressLint("ShowToast")
val activityManagementModule = module {

    single(override = true) { (activity: SermonsActivity) ->
        SermonsActivityImpl(activity)
    }

    single(override = true) { (activity: SermonsActivity, container: ViewGroup, savedInstanceState: Bundle) ->
        Conductor.attachRouter(activity, container, savedInstanceState)
    }

    single(override = true) {
        RouterTransaction.with(SermonsController())
    }

    factory(override = true) { (context: Context, @StringRes message: Int, duration: Int) ->
        Toast.makeText(context, context.getString(message), duration)
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

val sermonsModule = listOf(activityManagementModule, dataModule, recyclerViewModule, rxJavaModule)
