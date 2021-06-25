package ru.sorokin.flyfilmsx.viewmodel

import ru.sorokin.flyfilmsx.model.Film

// Улучшенный enum с возможностью переопределения конструктора своих наследников
sealed class AppState {
    data class Success(val filmData: List<Film>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
