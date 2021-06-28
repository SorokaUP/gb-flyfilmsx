package ru.sorokin.flyfilmsx.model

import java.util.*
import kotlin.collections.ArrayList

class Repository : IRepository {
    override fun getFilmsFromServer() = ArrayList<Film>()

    override fun getFilmsFromLocalStorage(): List<Film> {
        var films = ArrayList<Film>()
        films.add(Film(550
            , "Бойцовский клуб"
            , "Лень много писать про него..."
            , Date()
            , "отстой"
            , ""
            , 0.0f))
        return films
    }
}