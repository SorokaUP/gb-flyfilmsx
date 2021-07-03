package ru.sorokin.flyfilmsx.viewmodel

import ru.sorokin.flyfilmsx.model.Film
import ru.sorokin.flyfilmsx.model.FilmDTO

// Улучшенный enum с возможностью переопределения конструктора своих наследников
sealed class AppState {
    data class Success(val filmData: List<FilmDTO>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
