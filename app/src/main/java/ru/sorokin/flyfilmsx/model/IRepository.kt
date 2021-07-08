package ru.sorokin.flyfilmsx.model

import ru.sorokin.flyfilmsx.viewmodel.ListFilmLoader

interface IRepository {
    fun getFilmsPopularFromServer(listener: ListFilmLoader.FilmLoaderListener)//: List<FilmDTO>
    fun getFilmsFromLocalStorage() : List<FilmDTO>
}