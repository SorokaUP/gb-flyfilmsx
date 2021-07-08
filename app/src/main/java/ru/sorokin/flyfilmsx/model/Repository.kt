package ru.sorokin.flyfilmsx.model

import ru.sorokin.flyfilmsx.viewmodel.ListFilmLoader
import kotlin.collections.ArrayList

class Repository : IRepository {
    override fun getFilmsPopularFromServer(listener: ListFilmLoader.FilmLoaderListener) {
        val loader = ListFilmLoader(listener, 1)
        loader.loadList()
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