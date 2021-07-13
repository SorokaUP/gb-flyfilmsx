package ru.sorokin.flyfilmsx.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.sorokin.flyfilmsx.App.App
import ru.sorokin.flyfilmsx.room.DBRepository
import ru.sorokin.flyfilmsx.room.IDBRepository

class LikeViewModel(
    // LiveData может подписывать кого либо на себя, говоря тем самым кому бы то нибыло об
    // изменениях внутри него. Конкретный экземпляр Модели для конкретного Fragment типв AppState
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val historyRepository: IDBRepository = DBRepository(App.getHistoryDao())
) : ViewModel() {

    fun getLiveData() = liveDataToObserve
    fun getFilmsLikeFromDataBase() = getDataLikeFromDataBase()

    private fun getDataLikeFromDataBase() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            val listFilmDTO = historyRepository.getAllLike()
            if (listFilmDTO.isNotEmpty()) {
                liveDataToObserve.postValue(AppState.Success(listFilmDTO))
            } else {
                liveDataToObserve.postValue(AppState.Error(Exception("DATABASE EMPTY")))
            }
        }.start()
    }
}