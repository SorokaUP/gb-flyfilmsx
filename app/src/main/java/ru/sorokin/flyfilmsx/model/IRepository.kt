package ru.sorokin.flyfilmsx.model

import retrofit2.http.*

interface IRepository {
    fun getFilmsFromLocalStorage() : List<FilmDTO>

    fun getFilmsPopularFromServer(callback: retrofit2.Callback<PopularListDTO>, pageId: Int)
    fun getFilmFromServer(callback: retrofit2.Callback<FilmDTO>, id: Int)
}