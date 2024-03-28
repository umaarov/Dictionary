package uz.umarov.translateapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.umarov.translateapp.data.models.DictionaryModel
import uz.umarov.translateapp.data.models.FavouritesModel
import uz.umarov.translateapp.domain.repo.MainRepository
import uz.umarov.translateapp.domain.repo.RoomRepository
import uz.umarov.translateapp.domain.resource.DictionaryStateUI

class DictionaryDataViewModel(
    private val repo: MainRepository,
    private val roomRepo: RoomRepository
) : ViewModel() {

    private val _dictionaryData = MutableStateFlow<DictionaryStateUI>(DictionaryStateUI.Loading)
    val dictionaryData: StateFlow<DictionaryStateUI> get() = _dictionaryData

    fun loadDictionaryData() = viewModelScope.launch {
        repo.getDictionaryData().collect {
            _dictionaryData.value = it
        }
    }

    fun toggleFavourite(dictionary: DictionaryModel) = viewModelScope.launch(IO) {
        if (dictionary.isFavourite) {
            dictionary.id.let { roomRepo.deleteFavouriteWord(it) }
        } else {
            val favModel = FavouritesModel(
                dictionary.id,
                dictionary.english,
                dictionary.transcript,
                dictionary.uzbek,
                dictionary.type,
                isFavourite = true
            )
            roomRepo.insertFavouriteWords(favModel)
        }
        dictionary.checkFavourite()
        repo.updateDictionary(dictionary)
    }

}