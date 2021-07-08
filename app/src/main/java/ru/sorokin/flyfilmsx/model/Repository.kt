package ru.sorokin.flyfilmsx.model

import kotlin.collections.ArrayList

class Repository : IRepository {
    override fun getFilmsPopularFromServer(callback: retrofit2.Callback<PopularListDTO>, pageId: Int) {
        RestApi.api.getFilmsPopular(pageId).enqueue(callback)
    }

    override fun getFilmFromServer(callback: retrofit2.Callback<FilmDTO>, id: Int) {
        RestApi.api.getFilm(id).enqueue(callback)
    }

    override fun getFilmsFromLocalStorage() : List<FilmDTO> {
        var films = ArrayList<FilmDTO>()
        films.add(FilmDTO(
            null,
            null,
            550,
            null,
            null,
            "Бойцовский клуб",
            0.0f,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        ))
        return films
    }
}