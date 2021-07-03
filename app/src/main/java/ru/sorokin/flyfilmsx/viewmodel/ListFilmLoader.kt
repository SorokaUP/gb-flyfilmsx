package ru.sorokin.flyfilmsx.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import ru.sorokin.flyfilmsx.BuildConfig
import ru.sorokin.flyfilmsx.model.FilmDTO
import ru.sorokin.flyfilmsx.model.PopularListDTO
import ru.sorokin.flyfilmsx.model.PopularResultDTO
import ru.sorokin.flyfilmsx.model.RestApi
import javax.net.ssl.HttpsURLConnection

class ListFilmLoader(
    private val listener: FilmLoaderListener?,
    private val pageId: Int = 1
) {
    interface FilmLoaderListener {
        fun onLoaded(listFilmDTO: List<FilmDTO>)
        fun onFailed(throwable: Throwable)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadList() {
        val query = "https://api.themoviedb.org/3/movie/popular?api_key=${BuildConfig.THEMOVIEDB_API_KEY}&language=en-US&page=${pageId}"
        RestApi.runQuery(query, object : RestApi.IRestApiListener {

            override fun onLoaded(res: String) {
                val popularListDTO: PopularListDTO = Gson().fromJson(res, PopularListDTO::class.java)
                val listFilmDTO = ArrayList<FilmDTO>()
                for (item: PopularResultDTO in popularListDTO.results) {
                    listFilmDTO.add(item.toFilmDTO())
                }
                listener?.onLoaded(listFilmDTO)
            }

            override fun onFailed(throwable: Throwable) {
                listener?.onFailed(throwable)
            }
        })
    }

    fun loadListWithOutThread() : List<FilmDTO> {
        val query = "https://api.themoviedb.org/3/movie/popular?api_key=${BuildConfig.THEMOVIEDB_API_KEY}&language=en-US&page=${pageId}"
        val res = RestApi.runQueryWithOutThread(query)
        val popularListDTO: PopularListDTO = Gson().fromJson(res, PopularListDTO::class.java)
        val listFilmDTO = ArrayList<FilmDTO>()
        for (item: PopularResultDTO in popularListDTO.results) {
            listFilmDTO.add(item.toFilmDTO())
        }
        return listFilmDTO
    }
}