package ru.sorokin.flyfilmsx.model

interface IRepository {
    fun getFilmsFromLocalStorage() : List<Film>

    fun getFilmsPopularFromServer(callback: retrofit2.Callback<ResponseList>, pageId: Int)
    fun getFilmFromServer(callback: retrofit2.Callback<Film>, id: Int)
    fun getFilmByNameLikeFromServer(callback: retrofit2.Callback<ResponseList>, page: Int, isAdult: Boolean, query: String)

    fun getFilmByNameLikeFromDataBase(query: String): List<Film>
}