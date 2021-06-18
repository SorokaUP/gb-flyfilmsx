package ru.sorokin.flyfilmsx

interface IRepository {
    fun getFilmsFromServer() : List<Film>
    fun getFilmsFromLocalStorage() : List<Film>
}