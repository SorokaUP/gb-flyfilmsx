package ru.sorokin.flyfilmsx.model

import ru.sorokin.flyfilmsx.BuildConfig

object RestApiMethods {
    const val ADDRESS_BASE = "https://api.themoviedb.org/"
    const val ENDPOINT = "3/movie"
    const val ADDRESS = "$ADDRESS_BASE$ENDPOINT/"

    // Не забудьте выполлнить замену {variable} на ваше значение
    const val popular = ADDRESS + "popular?api_key=${BuildConfig.THEMOVIEDB_API_KEY}&page={pageId}&language=en-US"
    const val film = ADDRESS + "{id}?api_key=${BuildConfig.THEMOVIEDB_API_KEY}"
}