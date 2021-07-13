package ru.sorokin.flyfilmsx.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.sorokin.flyfilmsx.App.App
import ru.sorokin.flyfilmsx.model.Film
import ru.sorokin.flyfilmsx.room.DBRepository
import ru.sorokin.flyfilmsx.room.IDBRepository

class FilmViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val historyRepository: IDBRepository = DBRepository(App.getHistoryDao())
) : ViewModel() {

    fun getLiveData() = liveDataToObserve
    fun getComment(film_id: Int) = getCommentFromDataBase(film_id)
    fun setComment(film_id: Int, comment: String) = setCommentInDataBase(film_id, comment)

    private fun getCommentFromDataBase(film_id: Int) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            val comment = historyRepository.getComment(film_id)

            if (!comment.isNullOrEmpty()) {
                val listFilmDTO = ArrayList<Film>()
                val film = Film.newEmpty()
                film.comment = comment
                listFilmDTO.add(film)
                liveDataToObserve.postValue(AppState.Success(listFilmDTO))
            } else {
                liveDataToObserve.postValue(AppState.Error(Exception("DATABASE EMPTY")))
            }
        }.start()
    }

    private fun setCommentInDataBase(film_id: Int, comment: String) {
        Thread {
            historyRepository.setComment(film_id, comment)
        }.start()
    }
}