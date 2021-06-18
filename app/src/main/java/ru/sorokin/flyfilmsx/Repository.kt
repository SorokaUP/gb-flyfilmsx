package ru.sorokin.flyfilmsx

class Repository : IRepository {
    override fun getFilmsFromServer(): List<Film> {
        return ArrayList()
    }

    override fun getFilmsFromLocalStorage(): List<Film> {
        return ArrayList()
    }
}