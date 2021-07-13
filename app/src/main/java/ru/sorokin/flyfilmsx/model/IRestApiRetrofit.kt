package ru.sorokin.flyfilmsx.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IRestApiRetrofit {
    @GET("movie/popular")
    fun getFilmsPopular(
        @Query("page") pageId: Int
    ): Call<ResponseList>

    @GET("movie/{id}")
    fun getFilm(
        @Path("id") id: Int
    ): Call<Film>

    @GET("search/movie")
    fun getFilmSearch(
        @Query("page") page: Int,
        @Query("include_adult") include_adult: Boolean,
        @Query("query") query: String
    ): Call<ResponseList>
}