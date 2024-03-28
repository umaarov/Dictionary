package uz.umarov.translateapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import uz.umarov.translateapp.data.prefs.DataStoreManager

class ThemeViewModel(private val dataStore: DataStoreManager) : ViewModel() {

    val getTheme = dataStore.getTheme().asLiveData(IO)

    fun setTheme(isDark: Boolean) = viewModelScope.launch {
        dataStore.setTheme(isDark)
    }

}