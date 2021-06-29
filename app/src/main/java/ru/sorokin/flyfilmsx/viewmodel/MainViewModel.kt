package ru.sorokin.flyfilmsx.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.sorokin.flyfilmsx.model.Repository

class MainViewModel(
    // LiveData может подписывать кого либо на себя, говоря тем самым кому бы то нибыло об
    // изменениях внутри него. Конкретный экземпляр Модели для конкретного Fragment типв AppState
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    // Источник данных для приложения. Сам Репозиторий берет данные от туда, от куда ему нужно,
    // он лишь получает и хранит данные
    private val repository: Repository = Repository()
) : ViewModel() {

    fun getLiveData() = liveDataToObserve
    fun getFilmsFromLocalSource() = getDataFromLocalSource()
    fun getFilmsFromServerSource() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            // Искусственная пауза для создания эффекта подгрузки данных с сервера
            Thread.sleep(1000)
            // В связи с тем, что данные попадают из отдельного потока, используется postValue
            liveDataToObserve.postValue(AppState.Success(repository.getFilmsFromLocalStorage()))
        }.start()
    }
}