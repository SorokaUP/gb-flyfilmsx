package ru.sorokin.flyfilmsx.room

import androidx.room.Query
import ru.sorokin.flyfilmsx.model.Film

class DBRepository(private val localDataSource: HistoryDao) : IDBRepository {
    override fun getAllHistory(): List<Film> {
        return convertHistoryEntityToFilm(localDataSource.all())
    }

    override fun getAllLike(): List<Film> {
        return convertHistoryEntityToFilm(localDataSource.getDataLike())
    }

    override fun saveEntity(film: Film) {
        localDataSource.insert(convertFilmToEntity(film))
    }

    override fun setIsLike(film_id: Int, is_like: Int) {
        localDataSource.setIsLike(film_id, is_like)
    }

    override fun getIsLike(film_id: Int): List<Film> {
        return convertHistoryEntityToFilm(localDataSource.getIsLike(film_id))
    }

    private fun convertHistoryEntityToFilm(entityList: List<HistoryEntity>): List<Film> {
        return entityList.map {
            val film = Film.newEmpty()
            film.id = it.film_id
            film.original_title = it.original_title
            film.poster_path = it.poster_path
            film.isLike = it.is_like == 1

            film
        }
    }

    private fun convertFilmToEntity(film: Film): HistoryEntity {
        return HistoryEntity(
            0,
            film.id,
            film.original_title ?: "",
            film.poster_path ?: "",
            if (film.isLike == true) {1} else {0}
        )
    }
}