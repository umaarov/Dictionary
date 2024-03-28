package uz.umarov.translateapp.domain.repo.impls

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import uz.umarov.translateapp.data.local.AppDao
import uz.umarov.translateapp.data.models.FavouritesModel
import uz.umarov.translateapp.domain.repo.RoomRepository

class RoomRepositoryImpl(private val appDao: AppDao) : RoomRepository {
    override suspend fun insertFavouriteWords(fav: FavouritesModel): Long =
        appDao.insertFavLetters(fav)

    override suspend fun deleteFavouriteWord(id: String?) = appDao.deleteFavWord(id)

    override suspend fun getAllFavourites(): Flow<List<FavouritesModel>> =
        appDao.getAllFavWords().flowOn(Dispatchers.IO)

    override fun getFavouritesCount(): Flow<Int> = appDao.getFavDataCount()

    override suspend fun deleteFavouriteByLogics(favouriteId: String) {
        val favourite = appDao.getFavWordsName(favouriteId)
        if (favourite != null && favourite.isFavourite) {
            val dictionary = appDao.getWordsById(favourite.id)
            if (dictionary != null) {
                appDao.updateDictionaryById(dictionary.id, false)
            }
        }
        appDao.deleteFavWord(favouriteId)
    }

}