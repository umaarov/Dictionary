package uz.umarov.translateapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.umarov.translateapp.data.models.DictionaryModel
import uz.umarov.translateapp.data.models.FavouritesModel


@Database(
    entities = [DictionaryModel::class, FavouritesModel::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

}