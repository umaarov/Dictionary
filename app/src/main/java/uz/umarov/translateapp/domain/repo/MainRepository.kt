package uz.umarov.translateapp.domain.repo

import kotlinx.coroutines.flow.Flow
import uz.umarov.translateapp.data.models.DictionaryModel
import uz.umarov.translateapp.domain.resource.DictionaryStateUI


interface MainRepository {

    suspend fun getDictionaryData(): Flow<DictionaryStateUI>

    suspend fun updateDictionary(words: DictionaryModel)

}