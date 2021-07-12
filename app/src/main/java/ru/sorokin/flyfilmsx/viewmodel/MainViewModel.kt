package ru.sorokin.flyfilmsx.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.sorokin.flyfilmsx.App.App
import ru.sorokin.flyfilmsx.model.Film
import ru.sorokin.flyfilmsx.model.PopularList
import ru.sorokin.flyfilmsx.model.PopularResult
import ru.sorokin.flyfilmsx.model.Repository
import ru.sorokin.flyfilmsx.room.DBRepository
import ru.sorokin.flyfilmsx.room.IDBRepository

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
    fun getFilmByNameLike(query: String) = getFilmByNameLikeFromDataBase(query)

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
            // Искусственная пауза для создания эффекта подгрузки данных с сервера
            Thread.sleep(1000)
            // В связи с тем, что данные попадают из отдельного потока, используется postValue
            liveDataToObserve.postValue(AppState.Success(repository.getFilmByNameLikeFromDataBase(query)))
        }.start()
    }

    private fun getDataFromServerSource() {
        liveDataToObserve.value = AppState.Loading
        repository.getFilmsPopularFromServer(callBack, 1)
    }

    private val callBack = object :
        Callback<PopularList> {

        override fun onResponse(call: Call<PopularList>, response: Response<PopularList>) {
            val popularList: PopularList? = response.body()
            liveDataToObserve.postValue(
                if (response.isSuccessful && popularList != null) {
                    val listFilmDTO = ArrayList<Film>()
                    for (item: PopularResult in popularList.results) {
                        listFilmDTO.add(item.toFilm())
                        historyRepository.saveEntity(item.toFilm())
                    }

                    AppState.Success(listFilmDTO)
                } else {
                    AppState.Error(Exception("SERVER_ERROR"))
                }
            )
        }

        override fun onFailure(call: Call<PopularList>, t: Throwable) {
            AppState.Error(t)
        }
    }
}