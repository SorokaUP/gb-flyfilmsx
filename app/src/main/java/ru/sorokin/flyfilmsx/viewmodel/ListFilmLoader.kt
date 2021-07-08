package ru.sorokin.flyfilmsx.viewmodel

import android.os.Build
import android.os.Handler
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import ru.sorokin.flyfilmsx.BuildConfig
import javax.net.ssl.HttpsURLConnection
import okhttp3.*
import ru.sorokin.flyfilmsx.model.*
import java.io.IOException

class ListFilmLoader(
    private val listener: FilmLoaderListener,
    private val pageId: Int = 1
) {
    interface FilmLoaderListener {
        fun onLoaded(listFilmDTO: List<FilmDTO>)
        fun onFailed(throwable: Throwable)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadList() {
        val query = RestApiMethods.popular.replace("{pageId}", pageId.toString())
        RestApi.runQuery(query, object : Callback {

            val handler: Handler = Handler()

            // Вызывается, если ответ от сервера пришёл
            @Throws(IOException::class)
            override fun onResponse(call: Call?, response: Response) {
                val serverResponse: String? = response.body()?.string()

                // Синхронизируем поток с потоком UI
                if (response.isSuccessful && serverResponse != null) {
                    val popularListDTO: PopularListDTO = Gson().fromJson(serverResponse, PopularListDTO::class.java)
                    val listFilmDTO = ArrayList<FilmDTO>()
                    for (item: PopularResultDTO in popularListDTO.results) {
                        listFilmDTO.add(item.toFilmDTO())
                    }

                    handler.post {
                        listener.onLoaded(listFilmDTO)
                    }
                } else {
                    handler.post {
                        listener.onFailed(Exception("Не удалось получить данные"))
                    }
                }
            }

            // Вызывается при сбое в процессе запроса на сервер
            override fun onFailure(call: Call?, e: IOException?) {
                handler.post {
                    if (e != null) {
                        listener.onFailed(e)
                    } else {
                        listener.onFailed(IOException())
                    }
                }
            }
        })
    }
}