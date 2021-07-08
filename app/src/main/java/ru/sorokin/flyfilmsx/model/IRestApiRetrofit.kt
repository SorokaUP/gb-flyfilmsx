package ru.sorokin.flyfilmsx.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IRestApiRetrofit {
    @GET("popular")
    fun getFilmsPopular(
        @Query("page") pageId: Int
    ): Call<PopularListDTO>

    @GET("{id}")
    fun getFilm(
        @Path("id") id: Int
    ): Call<FilmDTO>
}