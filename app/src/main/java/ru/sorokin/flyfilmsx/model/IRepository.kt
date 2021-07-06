package ru.sorokin.flyfilmsx.model

interface IRepository {
    fun getFilmsFromServer() : List<FilmDTO>
    fun getFilmsFromLocalStorage() : List<FilmDTO>
}