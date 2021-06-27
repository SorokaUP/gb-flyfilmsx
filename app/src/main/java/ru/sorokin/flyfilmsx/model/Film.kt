package ru.sorokin.flyfilmsx.model

import java.util.*
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

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

data class FilmDTO(
    val genres: List<GenreDTO>?,
    val homepage: String?,
    val id: Int,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Float?,
    val poster_path: String?,
    val production_companies: List<ProductionCompaniesDTO>?,
    val production_countries: List<ProductionCountriesDTO>?,
    val release_date: Date?,
    val runtime: Int?,
    val spoken_languages: List<SpokenLanguageDTO>?,
    val status: String?,
    val tagline: String?
) {
    fun genresToString() : String {
        var res = ""
        genres?.let {
            it.forEach() { res += if (res == "") it.name else "; " + it.name }
        }
        return res
    }
}

data class GenreDTO(
    val id: Int,
    val name: String
)

data class ProductionCompaniesDTO (
    val id: Int,
    val logo_path: String,
    val name: String,
    val origin_country: String
)

data class ProductionCountriesDTO (
    val iso_3166_1: String,
    val name: String
)

data class SpokenLanguageDTO (
    val english_name: String,
    val iso_639_1: String,
    val name: String
)