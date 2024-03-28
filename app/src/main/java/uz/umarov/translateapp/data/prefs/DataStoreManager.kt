package uz.umarov.translateapp.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import uz.umarov.translateapp.utils.Constants.THEME_KEY
import java.io.IOException


class DataStoreManager(context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = THEME_KEY)
    private val dataStore = context.dataStore

    companion object {
        private val darkModeKey = booleanPreferencesKey("DARK_MODE_KEY")
    }

    suspend fun setTheme(isDark: Boolean) = dataStore.edit { pref ->
        pref[darkModeKey] = isDark
    }

    fun getTheme(): Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { pref ->
            pref[darkModeKey] ?: false
        }

}