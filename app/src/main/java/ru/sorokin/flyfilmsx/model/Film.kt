package ru.sorokin.flyfilmsx.model

import java.util.*
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Film (
    val id: Int,
    val caption: String,
    val description: String,
    val dateFrom: Date,
    val tags: String,
    val posterPath: String,
    val rate:Float
    ) : Parcelable

@Parcelize
data class FilmDTO (
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
    val release_date: String?,
    val runtime: Int?,
    val spoken_languages: List<SpokenLanguageDTO>?,
    val status: String?,
    val tagline: String?
) : Parcelable {
    fun genresToString() : String {
        var res = ""
        genres?.let {
            it.forEach() { res += if (res == "") it.name else "; " + it.name }
        }
        return res
    }
}

@Parcelize
data class GenreDTO(
    val id: Int,
    val name: String
) : Parcelable

@Parcelize
data class ProductionCompaniesDTO (
    val id: Int,
    val logo_path: String,
    val name: String,
    val origin_country: String
) : Parcelable

@Parcelize
data class ProductionCountriesDTO (
    val iso_3166_1: String,
    val name: String
) : Parcelable

@Parcelize
data class SpokenLanguageDTO (
    val english_name: String,
    val iso_639_1: String,
    val name: String
) : Parcelable



data class PopularListDTO (
    val page: Int,
    val results: List<PopularResultDTO>,
    val total_pages: Int,
    val total_results: Int
)

data class PopularResultDTO (
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Float,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Float,
    val vote_count: Int
) {
    fun toFilmDTO() = FilmDTO (
        null,
        null,
        id,
        original_language,
        original_title,
        overview,
        popularity,
        poster_path,
        null,
        null,
        release_date,
        null,
        null,
        null,
        null
    )
}