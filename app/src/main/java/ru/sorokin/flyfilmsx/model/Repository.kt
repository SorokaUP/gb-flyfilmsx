package ru.sorokin.flyfilmsx.model

import java.util.*
import kotlin.collections.ArrayList

class Repository : IRepository {
    override fun getFilmsFromServer(): List<Film> {
        return ArrayList()
    }

    override fun getFilmsFromLocalStorage(): List<Film> {
        var films = ArrayList<Film>()
        films.add(Film(0, "Test caption", "Test description", Date(), "test; tags;", ""))
        return films
    }
}