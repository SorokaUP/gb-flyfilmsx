package ru.sorokin.flyfilmsx.model

interface IRepository {
    fun getFilmsFromServer() : List<Film>
    fun getFilmsFromLocalStorage() : List<Film>
}