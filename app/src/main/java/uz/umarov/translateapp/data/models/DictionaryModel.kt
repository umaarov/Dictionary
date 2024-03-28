package uz.umarov.translateapp.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import uz.umarov.translateapp.utils.Constants.CACHE_TABLE_NAME


@Entity(tableName = CACHE_TABLE_NAME)
@Parcelize
data class DictionaryModel(
    @ColumnInfo(name = "words_id")
    val id: String,

    @ColumnInfo(name = "english_word")
    val english: String,

    @ColumnInfo(name = "uzbek_word")
    val uzbek: String,

    @ColumnInfo(name = "words_transcript")
    val transcript: String,

    @ColumnInfo(name = "words_type")
    val type: String,

    @ColumnInfo(name = "is_favourite")
    var isFavourite: Boolean = false
) : Parcelable {

    @IgnoredOnParcel
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var localID: Int? = null

    fun checkFavourite() {
        isFavourite = !isFavourite
    }

}