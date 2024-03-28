package uz.umarov.translateapp.domain.repo.impls

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.umarov.translateapp.data.local.AppDao
import uz.umarov.translateapp.data.models.DictionaryModel
import uz.umarov.translateapp.data.source.LocalDataSource
import uz.umarov.translateapp.domain.repo.MainRepository
import uz.umarov.translateapp.domain.resource.DictionaryStateUI


class MainRepositoryImpl(private val local: LocalDataSource, private val appDao: AppDao) :
    MainRepository {

    override suspend fun getDictionaryData(): Flow<DictionaryStateUI> = flow {
        emit(DictionaryStateUI.Loading)
        try {
            val cachingData = appDao.getAllWords()
            if (cachingData.isNotEmpty()) {
                emit(DictionaryStateUI.Success(cachingData))
            } else {
                val newData = local.getItemsFromJson()
                newData.let { appDao.deleteAndInsert(it) }
                emit(DictionaryStateUI.Success(newData))
            }
        } catch (e: Exception) {
            emit(DictionaryStateUI.Error(e.localizedMessage ?: "Unknown Error"))
        }
    }.catch {
        emit(DictionaryStateUI.Error(it.localizedMessage ?: "Unknown Error"))
    }.flowOn(Dispatchers.IO)

    override suspend fun updateDictionary(words: DictionaryModel) = appDao.updateDictionary(words)

}