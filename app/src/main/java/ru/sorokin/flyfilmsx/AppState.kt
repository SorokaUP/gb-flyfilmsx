package ru.sorokin.flyfilmsx

sealed class AppState {
    data class Success(val filmData: Any) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
