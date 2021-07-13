package ru.sorokin.flyfilmsx.room

import ru.sorokin.flyfilmsx.model.Film

interface IDBRepository {
    fun getAllHistory(): List<Film>
    fun getAllLike(): List<Film>
    fun saveEntity(film: Film)

    fun setIsLike(film_id: Int, is_like: Int)
    fun getIsLike(film_id: Int): List<Film>

    fun setComment(film_id: Int, comment: String)
    fun getComment(film_id: Int): String

    fun getFilmByNameLike(title: String): List<Film>
}