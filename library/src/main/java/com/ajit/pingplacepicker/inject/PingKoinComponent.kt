package com.ajit.pingplacepicker.inject

import android.content.Context
import com.ajit.pingplacepicker.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.dsl.koinApplication
import org.koin.core.logger.Level

object PingKoinContext {

    private lateinit var appContext: Context

    val koin: Koin by lazy {
        koinApplication {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(appContext)
            modules(listOf(repositoryModule, viewModelModule))
        }.koin
    }

    /**
     * Initializes the Dependency Injection framework by passing
     * the current application context.
     */
    @Synchronized
    fun init(context: Context) {
        appContext = context.applicationContext
    }
}

interface PingKoinComponent : KoinComponent {

    override fun getKoin(): Koin = PingKoinContext.koin

}
