package ru.sorokin.flyfilmsx.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.sorokin.flyfilmsx.App.App
import ru.sorokin.flyfilmsx.model.Film
import ru.sorokin.flyfilmsx.model.ResponseList
import ru.sorokin.flyfilmsx.model.ResponseResult
import ru.sorokin.flyfilmsx.model.Repository
import ru.sorokin.flyfilmsx.room.DBRepository
import ru.sorokin.flyfilmsx.room.IDBRepository
import ru.sorokin.flyfilmsx.view.IS_SHOW_18_PLUS

class MainViewModel(
    // LiveData может подписывать кого либо на себя, говоря тем самым кому бы то нибыло об
    // изменениях внутри него. Конкретный экземпляр Модели для конкретного Fragment типв AppState
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    // Источник данных для приложения. Сам Репозиторий берет данные от туда, от куда ему нужно,
    // он лишь получает и хранит данные
    private val repository: Repository = Repository(),
    private val historyRepository: IDBRepository = DBRepository(App.getHistoryDao())
) : ViewModel() {

    fun getLiveData() = liveDataToObserve
    fun getFilmsFromLocalSource() = getDataFromLocalSource()
    fun getFilmsFromServerSource() = getDataFromServerSource()
    fun getFilmByNameLike(query: String, isAdult: Boolean) = getFilmByNameLikeFromServer(query, isAdult)//getFilmByNameLikeFromDataBase(query)

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            // Искусственная пауза для создания эффекта подгрузки данных с сервера
            Thread.sleep(1000)
            // В связи с тем, что данные попадают из отдельного потока, используется postValue
            liveDataToObserve.postValue(AppState.Success(repository.getFilmsFromLocalStorage()))
        }.start()
    }

    private fun getFilmByNameLikeFromDataBase(query: String) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            liveDataToObserve.postValue(AppState.Success(repository.getFilmByNameLikeFromDataBase(query)))
        }.start()
    }

    private fun getFilmByNameLikeFromServer(query: String, isAdult: Boolean) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            repository.getFilmByNameLikeFromServer(callBack, 1, isAdult, query)
        }.start()
    }

    private fun getDataFromServerSource() {
        liveDataToObserve.value = AppState.Loading
        repository.getFilmsPopularFromServer(callBack, 1)
    }

    private val callBack = object :
        Callback<ResponseList> {

        override fun onResponse(call: Call<ResponseList>, response: Response<ResponseList>) {
            val resList: ResponseList? = response.body()
            liveDataToObserve.postValue(
                if (response.isSuccessful && resList != null) {
                    val listFilmDTO = ArrayList<Film>()
                    for (item: ResponseResult in resList.results) {
                        listFilmDTO.add(item.toFilm())
                        // Получая новый фильм, сохраняем его
                        historyRepository.saveEntity(item.toFilm())
                    }

                    AppState.Success(listFilmDTO)
                } else {
                    AppState.Error(Exception("SERVER_ERROR"))
                }
            )
        }

        override fun onFailure(call: Call<ResponseList>, t: Throwable) {
            AppState.Error(t)
        }
    }
}