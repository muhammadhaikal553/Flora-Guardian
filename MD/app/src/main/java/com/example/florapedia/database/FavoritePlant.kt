package com.example.florapedia.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoritePlant(
    @PrimaryKey(autoGenerate = false)

    @ColumnInfo(name = "plantFamily")
    var plantFamily: String = "",

    @ColumnInfo(name = "attachment")
    var attachment: String? = null,
) : Parcelable
{
    companion object {
        const val EXTRA_FAVORITE = "favorite_item"
    }
}
