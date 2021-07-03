package ru.sorokin.flyfilmsx.model

import ru.sorokin.flyfilmsx.viewmodel.ListFilmLoader
import kotlin.collections.ArrayList

class Repository : IRepository {
    override fun getFilmsFromServer(): List<FilmDTO> {
        val loader = ListFilmLoader(null, 1)
        return loader.loadListWithOutThread()
    }

    override fun getFilmsFromLocalStorage(): List<FilmDTO> {
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