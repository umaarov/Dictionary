package uz.umarov.translateapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import uz.umarov.translateapp.data.prefs.DataStoreManager
import uz.umarov.translateapp.di.appModule
import uz.umarov.translateapp.di.viewModelModule

class App : Application(), KoinComponent {

    companion object {
        private lateinit var instance: App
        private lateinit var dataStoreManager: DataStoreManager
    }

    override fun onCreate() {
        super.onCreate()
        instance = this@App

        dataStoreManager = DataStoreManager(instance)

        startKoin {
            androidLogger()
            androidContext(instance)
            modules(appModule, viewModelModule)
        }

    }

}