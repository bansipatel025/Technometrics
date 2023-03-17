package com.technometrics


import android.app.Application
import androidx.multidex.MultiDexApplication
import com.technometrics.data.api.RetrofitClient
import com.technometrics.data.repository.AppRepository
import com.technometrics.ui.factory.MovieDetailFactory
import com.technometrics.ui.factory.DashboardFactory
import com.technometrics.ui.factory.SplashFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

open class Application : KodeinAware, MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        application = this
    }

    companion object {
        @JvmStatic
        var application: Application? = null
            private set
    }

    //kodein injection provide by kotlin
    override val kodein = Kodein.lazy {
        import(androidXModule(this@Application))
        bind() from singleton { RetrofitClient() }
        bind() from provider {
            SplashFactory(
                instance()
            )
        }
        bind() from provider {
            DashboardFactory(
                instance(), appRepository = AppRepository()
            )
        }
        bind() from provider {
            MovieDetailFactory(
                instance(), appRepository = AppRepository()
            )
        }

    }
}