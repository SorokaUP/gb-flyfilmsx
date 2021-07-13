package ru.sorokin.flyfilmsx.model

import ru.sorokin.flyfilmsx.App.App
import ru.sorokin.flyfilmsx.room.DBRepository
import ru.sorokin.flyfilmsx.room.IDBRepository
import kotlin.collections.ArrayList

class Repository : IRepository {
    private val historyRepository: IDBRepository = DBRepository(App.getHistoryDao())

    override fun getFilmsPopularFromServer(callback: retrofit2.Callback<ResponseList>, pageId: Int) {
        RestApi.api.getFilmsPopular(pageId).enqueue(callback)
    }

    override fun getFilmFromServer(callback: retrofit2.Callback<Film>, id: Int) {
        RestApi.api.getFilm(id).enqueue(callback)
    }

    override fun getFilmByNameLikeFromServer(callback: retrofit2.Callback<ResponseList>, page: Int, isAdult: Boolean, query: String) {
        if (query.isEmpty()) {
            getFilmsPopularFromServer(callback, page)
        } else {
            RestApi.api.getFilmSearch(page, isAdult, query).enqueue(callback)
        }
    }

    override fun getFilmByNameLikeFromDataBase(query: String): List<Film> {
        return historyRepository.getFilmByNameLike(query);
    }

    override fun getFilmsFromLocalStorage() : List<Film> {
        val films = ArrayList<Film>()
        val film: Film = Film.newEmpty()
        film.id = 550
        film.original_title = "Бойцовский клуб"
        film.popularity = 0.0f
        films.add(film)

        return films
    }
}