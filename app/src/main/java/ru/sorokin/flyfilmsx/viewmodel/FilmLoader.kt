package ru.sorokin.flyfilmsx.viewmodel

import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import ru.sorokin.flyfilmsx.BuildConfig
import ru.sorokin.flyfilmsx.model.FilmDTO
import ru.sorokin.flyfilmsx.model.RestApi
import ru.sorokin.flyfilmsx.model.RestApiMethods
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection
import okhttp3.*
import java.io.IOException
import kotlin.concurrent.thread

class FilmLoader (
    private val listener: FilmLoaderListener,
    private val id: Int
) {
    interface FilmLoaderListener {
        fun onLoaded(filmDTO: FilmDTO)
        fun onFailed(throwable: Throwable)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadFilm() {
        val query = RestApiMethods.film.replace("{id}", id.toString())
        RestApi.runQuery(query, object : Callback {
            val handler: Handler = Handler()

            override fun onResponse(call: Call, response: Response) {
                val serverResponse: String? = response.body()?.string()
                val filmDTO: FilmDTO = Gson().fromJson(serverResponse, FilmDTO::class.java)

                handler.post {
                    listener.onLoaded(filmDTO)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                handler.post {
                    listener.onFailed(e)
                }
            }
        })
    }
}