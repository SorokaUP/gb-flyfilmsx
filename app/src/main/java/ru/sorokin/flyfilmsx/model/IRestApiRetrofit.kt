package ru.sorokin.flyfilmsx.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IRestApiRetrofit {
    @GET("popular")
    fun getFilmsPopular(
        @Query("page") pageId: Int
    ): Call<PopularList>

    @GET("{id}")
    fun getFilm(
        @Path("id") id: Int
    ): Call<Film>

    @GET("search/movie")
    fun getFilmSearch(
        @Query("page") page: Int,
        @Query("include_adult") include_adult: Boolean,
        @Query("query") query: String
    ): Call<Film>
}