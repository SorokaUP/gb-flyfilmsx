package ru.sorokin.flyfilmsx.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.sorokin.flyfilmsx.model.Repository

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: Repository = Repository()
) : ViewModel() {


    fun getLiveData() = liveDataToObserve
    fun getFilms() = getDataFromLocalSource()

    fun getData(): LiveData<AppState> {
        getDataFromLocalSource()
        return liveDataToObserve
    }

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            Thread.sleep(3000)
            liveDataToObserve.postValue(AppState.Success(repository.getFilmsFromLocalStorage()))
        }.start()
    }
}