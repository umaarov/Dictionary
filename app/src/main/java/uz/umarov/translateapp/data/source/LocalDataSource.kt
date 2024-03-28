package uz.umarov.translateapp.data.source

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.umarov.translateapp.data.models.DictionaryModel

class LocalDataSource(private val context: Context) {


    fun getItemsFromJson(): List<DictionaryModel> {

        return try {
            val json = context.assets.open("dictionary.json").bufferedReader().use { it.readText() }
            parseItemsJson(json)
        } catch (e: Exception) {
            Log.e("LocalDataSource", "Error: $e")
            emptyList()
        }
    }

    private fun parseItemsJson(json: String): List<DictionaryModel> {
        val typeToken = object : TypeToken<List<DictionaryModel>>() {}.type
        return Gson().fromJson(json, typeToken)
    }

}