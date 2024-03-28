package uz.umarov.translateapp.domain.repo

import kotlinx.coroutines.flow.Flow
import uz.umarov.translateapp.data.models.FavouritesModel


interface RoomRepository {

    suspend fun insertFavouriteWords(fav: FavouritesModel): Long

    suspend fun deleteFavouriteWord(id: String?)

    suspend fun getAllFavourites(): Flow<List<FavouritesModel>>

    fun getFavouritesCount(): Flow<Int>

    suspend fun deleteFavouriteByLogics(favouriteId: String)

}