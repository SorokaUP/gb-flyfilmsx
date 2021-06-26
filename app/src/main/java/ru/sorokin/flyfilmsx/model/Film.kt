package ru.sorokin.flyfilmsx.model

import java.util.*
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Film(
    val id: Int,
    val caption: String,
    val description: String,
    val dateFrom: Date,
    val tags: String,
    val posterPath: String,
    val rate:Float
    ) : Parcelable
