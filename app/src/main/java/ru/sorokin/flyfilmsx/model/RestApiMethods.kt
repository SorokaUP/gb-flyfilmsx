package ru.sorokin.flyfilmsx.model

import ru.sorokin.flyfilmsx.BuildConfig

object RestApiMethods {
    const val ADDRESS_BASE_API = "https://api.themoviedb.org/"
    const val ADDRESS_BASE_SITE = "https://www.themoviedb.org/"
    const val ENDPOINT = "3/movie"
    const val ADDRESS = "$ADDRESS_BASE_API$ENDPOINT/"
    const val IMAGE_PATH = "t/p/w600_and_h900_bestv2"
    const val ADDRESS_IMAGE_600X900 = "$ADDRESS_BASE_SITE$IMAGE_PATH"

    // Не забудьте выполлнить замену {variable} на ваше значение
    const val popular = ADDRESS_BASE_API + "popular?api_key=${BuildConfig.THEMOVIEDB_API_KEY}&page={pageId}&language=en-US"
    const val film = ADDRESS_BASE_API + "{id}?api_key=${BuildConfig.THEMOVIEDB_API_KEY}"
}