package ru.sorokin.flyfilmsx.model

interface IRepository {
    fun getFilmsFromLocalStorage() : List<Film>

    fun getFilmsPopularFromServer(callback: retrofit2.Callback<PopularList>, pageId: Int)
    fun getFilmFromServer(callback: retrofit2.Callback<Film>, id: Int)
}