package uz.umarov.translateapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import uz.umarov.translateapp.utils.Constants.FAV_TABLE_NAME


@Entity(tableName = FAV_TABLE_NAME)
@Parcelize
data class FavouritesModel(
    val id: String,
    val english: String,
    val transcript: String,
    val uzbek: String,
    val type: String,
    val isFavourite: Boolean = false
) : Parcelable {

    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var favID: Int? = null

}