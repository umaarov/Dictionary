package uz.umarov.translateapp.domain.resource

import uz.umarov.translateapp.data.models.DictionaryModel


sealed class DictionaryStateUI {

    data object Loading : DictionaryStateUI()
    data class Success(val dictionaryData: List<DictionaryModel>) : DictionaryStateUI()
    data class Error(val errorMessage: String) : DictionaryStateUI()

}
