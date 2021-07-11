package ru.sorokin.flyfilmsx.model

import kotlin.collections.ArrayList

class Repository : IRepository {
    override fun getFilmsPopularFromServer(callback: retrofit2.Callback<PopularList>, pageId: Int) {
        RestApi.api.getFilmsPopular(pageId).enqueue(callback)
    }

    override fun getFilmFromServer(callback: retrofit2.Callback<Film>, id: Int) {
        RestApi.api.getFilm(id).enqueue(callback)
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