package ru.sorokin.flyfilmsx.viewmodel

import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import ru.sorokin.flyfilmsx.BuildConfig
import ru.sorokin.flyfilmsx.model.FilmDTO
import ru.sorokin.flyfilmsx.model.RestApi
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

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
        val query = "https://api.themoviedb.org/3/movie/${id}?api_key=${BuildConfig.THEMOVIEDB_API_KEY}"
        RestApi.runQuery(query, object : RestApi.IRestApiListener {

            override fun onLoaded(res: String) {
                val filmDTO: FilmDTO = Gson().fromJson(res, FilmDTO::class.java)
                listener.onLoaded(filmDTO)
            }

            override fun onFailed(throwable: Throwable) {
                listener.onFailed(throwable)
            }
        })
    }
}