package ru.sorokin.flyfilmsx.model

import ru.sorokin.flyfilmsx.BuildConfig

private const val ADDRESS = "https://api.themoviedb.org/3/movie/"

object RestApiMethods {
    // Не забудьте выполлнить замену {variable} на ваше значение
    const val popular = ADDRESS + "popular?api_key=${BuildConfig.THEMOVIEDB_API_KEY}&page={pageId}&language=en-US"
    const val film = ADDRESS + "{id}?api_key=${BuildConfig.THEMOVIEDB_API_KEY}"
}