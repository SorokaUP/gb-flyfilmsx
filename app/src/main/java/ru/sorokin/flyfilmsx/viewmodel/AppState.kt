package ru.sorokin.flyfilmsx.viewmodel

import ru.sorokin.flyfilmsx.model.Film

sealed class AppState {
    data class Success(val filmData: List<Film>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
