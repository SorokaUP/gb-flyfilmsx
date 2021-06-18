package ru.sorokin.flyfilmsx.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.sorokin.flyfilmsx.AppState

class MainViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()) : ViewModel() {
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
            liveDataToObserve.postValue(AppState.Success(Any()))
        }.start()
    }
}