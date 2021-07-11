package ru.sorokin.flyfilmsx.room

import ru.sorokin.flyfilmsx.model.Film

interface IDBRepository {
    fun getAllHistory(): List<Film>
    fun getAllLike(): List<Film>
    fun saveEntity(film: Film)

    fun setIsLike(film_id: Int, is_like: Int)
    fun getIsLike(film_id: Int): List<Film>
}