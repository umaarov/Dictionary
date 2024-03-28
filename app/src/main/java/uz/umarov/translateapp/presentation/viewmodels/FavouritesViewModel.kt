package uz.umarov.translateapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.umarov.translateapp.data.models.FavouritesModel
import uz.umarov.translateapp.domain.repo.MainRepository
import uz.umarov.translateapp.domain.repo.RoomRepository

class FavouritesViewModel(
    private val roomRepo: RoomRepository,
    private val dictionaryRepo: MainRepository
) : ViewModel() {

    private val _favouriteWords = MutableStateFlow<List<FavouritesModel>>(emptyList())
    val favouriteWords: StateFlow<List<FavouritesModel>> get() = _favouriteWords

    fun getAllFavourites() = viewModelScope.launch {
        roomRepo.getAllFavourites().collect {
            _favouriteWords.value = it
        }
    }

    val countedData: LiveData<Int> = roomRepo.getFavouritesCount().asLiveData(IO)

    fun deleteFavWord(id: String) = viewModelScope.launch(IO) {
        roomRepo.deleteFavouriteByLogics(id)
    }

}