package ru.sorokin.flyfilmsx.viewmodel

import android.net.sip.SipErrorCode.SERVER_ERROR
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.sorokin.flyfilmsx.model.FilmDTO
import ru.sorokin.flyfilmsx.model.PopularListDTO
import ru.sorokin.flyfilmsx.model.PopularResultDTO
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
    fun getFilmsFromServerSource() = getDataFromServerSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            // Искусственная пауза для создания эффекта подгрузки данных с сервера
            Thread.sleep(1000)
            // В связи с тем, что данные попадают из отдельного потока, используется postValue
            liveDataToObserve.postValue(AppState.Success(repository.getFilmsFromLocalStorage()))
        }.start()
    }

    private fun getDataFromServerSource() {
        liveDataToObserve.value = AppState.Loading
        repository.getFilmsPopularFromServer(callBack, 1)
    }

    private val callBack = object :
        Callback<PopularListDTO> {

        override fun onResponse(call: Call<PopularListDTO>, response: Response<PopularListDTO>) {
            val popularListDTO: PopularListDTO? = response.body()
            liveDataToObserve.postValue(
                if (response.isSuccessful && popularListDTO != null) {
                    val listFilmDTO = ArrayList<FilmDTO>()
                    for (item: PopularResultDTO in popularListDTO.results) {
                        listFilmDTO.add(item.toFilmDTO())
                    }

                    AppState.Success(listFilmDTO)
                } else {
                    AppState.Error(Exception("SERVER_ERROR"))
                }
            )
        }

        override fun onFailure(call: Call<PopularListDTO>, t: Throwable) {
            AppState.Error(t)
        }
    }
}