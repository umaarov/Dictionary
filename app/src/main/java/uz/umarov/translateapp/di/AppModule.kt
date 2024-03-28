package uz.umarov.translateapp.di

import androidx.room.Room
import uz.umarov.translateapp.presentation.viewmodels.DictionaryDataViewModel
import uz.umarov.translateapp.presentation.viewmodels.FavouritesViewModel
import uz.umarov.translateapp.presentation.viewmodels.ThemeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uz.umarov.translateapp.data.local.AppDatabase
import uz.umarov.translateapp.data.prefs.DataStoreManager
import uz.umarov.translateapp.data.source.LocalDataSource
import uz.umarov.translateapp.domain.repo.MainRepository
import uz.umarov.translateapp.domain.repo.RoomRepository
import uz.umarov.translateapp.domain.repo.impls.MainRepositoryImpl
import uz.umarov.translateapp.domain.repo.impls.RoomRepositoryImpl


val appModule = module {

    single {
        Room.databaseBuilder(
            androidContext(), AppDatabase::class.java, "DictionaryApp.db"
        ).build()
    }

    single { get<AppDatabase>().appDao() }

    single { DataStoreManager(androidContext()) }

    single { LocalDataSource(androidContext()) }

    factory<MainRepository> { MainRepositoryImpl(get(), get()) }

    factory<RoomRepository> { RoomRepositoryImpl(get()) }

}

val viewModelModule = module {

    viewModel {
        ThemeViewModel(get())
    }

    viewModel {
        DictionaryDataViewModel(get(), get())
    }

    viewModel {
        FavouritesViewModel(get(), get())
    }
}