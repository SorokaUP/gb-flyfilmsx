package ru.sorokin.flyfilmsx.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Film (
    var genres: List<Genre>?,
    var homepage: String?,
    var id: Int,
    var original_language: String?,
    var original_title: String?,
    var overview: String?,
    var popularity: Float?,
    var poster_path: String?,
    var production_companies: List<ProductionCompanies>?,
    var production_countries: List<ProductionCountries>?,
    var release_date: String?,
    var runtime: Int?,
    var spoken_languages: List<SpokenLanguage>?,
    var status: String?,
    var tagline: String?,

    // Custom fields
    var isLike: Boolean?,
    var adult: Boolean?
) : Parcelable {

    companion object StaticFun {
        fun newEmpty() = Film(
            null,
            null,
            0,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,

            null,
            null
        )
    }

    fun genresToString() : String {
        var res = ""
        genres?.let {
            it.forEach() { res += if (res == "") it.name else "; " + it.name }
        }
        return res
    }
}

@Parcelize
data class Genre(
    val id: Int,
    val name: String
) : Parcelable

@Parcelize
data class ProductionCompanies (
    val id: Int,
    val logo_path: String,
    val name: String,
    val origin_country: String
) : Parcelable

@Parcelize
data class ProductionCountries (
    val iso_3166_1: String,
    val name: String
) : Parcelable

@Parcelize
data class SpokenLanguage (
    val english_name: String,
    val iso_639_1: String,
    val name: String
) : Parcelable



data class PopularList (
    val page: Int,
    val results: List<PopularResult>,
    val total_pages: Int,
    val total_results: Int
)

data class PopularResult (
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
    fun toFilm(): Film {
        val film: Film = Film.newEmpty()
        film.id = id
        film.original_language = original_language
        film.original_title = original_title
        film.overview = overview
        film.popularity = popularity
        film.poster_path = poster_path
        film.release_date = release_date
        film.adult = adult

        return film
    }
}