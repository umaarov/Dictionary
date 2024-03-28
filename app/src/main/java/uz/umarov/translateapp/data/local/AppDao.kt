package uz.umarov.translateapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import uz.umarov.translateapp.utils.Constants.CACHE_TABLE_NAME
import kotlinx.coroutines.flow.Flow
import uz.umarov.translateapp.data.models.DictionaryModel
import uz.umarov.translateapp.data.models.FavouritesModel
import uz.umarov.translateapp.utils.Constants.FAV_TABLE_NAME


@Dao
interface AppDao {

    /** cached data category **/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWords(data: List<DictionaryModel>)

    @Query("UPDATE $CACHE_TABLE_NAME SET is_favourite = :fav WHERE words_id = :id")
    suspend fun updateDictionaryById(id: String, fav: Boolean)

    @Update
    suspend fun updateDictionary(words: DictionaryModel)

    @Query("SELECT * FROM $CACHE_TABLE_NAME WHERE words_id = :id")
    fun getWordsById(id: String?): DictionaryModel?

    @Query("SELECT * FROM $CACHE_TABLE_NAME")
    fun getAllWords(): List<DictionaryModel>

    @Query("DELETE FROM $CACHE_TABLE_NAME WHERE words_id = :id")
    suspend fun deleteWord(id: String?)

    @Query("DELETE FROM $CACHE_TABLE_NAME")
    fun deleteAll()

    @Transaction
    suspend fun deleteAndInsert(data: List<DictionaryModel>) {
        deleteAll()
        insertWords(data)
    }

    /** favourites category **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavLetters(favouritesModel: FavouritesModel): Long

    @Query("SELECT * FROM $FAV_TABLE_NAME ORDER BY english ASC")
    fun getAllFavWords(): Flow<List<FavouritesModel>>

    @Query("SELECT * FROM $FAV_TABLE_NAME WHERE id = :id")
    fun getFavWordsName(id: String?): FavouritesModel?

    @Query("DELETE FROM $FAV_TABLE_NAME")
    suspend fun deleteAllFavWords()

    @Query("SELECT COUNT(*) FROM $FAV_TABLE_NAME")
    fun getFavDataCount(): Flow<Int>

    @Query("DELETE FROM $FAV_TABLE_NAME WHERE id = :id")
    suspend fun deleteFavWord(id: String?)

    @Query("SELECT * FROM $FAV_TABLE_NAME")
    fun getFavouritesForQuiz(): List<FavouritesModel>
    /*******************************************************/

}