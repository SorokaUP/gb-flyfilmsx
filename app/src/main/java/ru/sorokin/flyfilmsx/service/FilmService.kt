package ru.sorokin.flyfilmsx.service

import android.app.IntentService
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import ru.sorokin.flyfilmsx.BuildConfig
import ru.sorokin.flyfilmsx.model.PopularListDTO
import ru.sorokin.flyfilmsx.model.RestApi
import java.io.BufferedReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors

const val PAGE_ID = "PageId"

class FilmService(name: String = "DetailService") : IntentService(name) {

    private val broadcastIntent = Intent(DETAILS_INTENT_FILTER)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {
            onEmptyIntent()
        } else {
            val pageId = intent.getIntExtra(PAGE_ID, 0)
            if (pageId == 0) {
                onEmptyData()
            } else {
                loadList(pageId)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadList(pageId: Int) {
        try {
            try {
                val query = "https://api.themoviedb.org/3/movie/popular?api_key=${BuildConfig.THEMOVIEDB_API_KEY}&language=en-US&page=${pageId}"
                val res = RestApi.runQueryWithOutThread(query, true)
                val popularListDTO: PopularListDTO = Gson().fromJson(res, PopularListDTO::class.java)
                onResponse(popularListDTO)
            } catch (e: Exception) {
                onErrorRequest(e.message ?: "Empty error")
            }
        } catch (e: MalformedURLException) {
            onMalformedURL()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    private fun onResponse(popularListDTO: PopularListDTO) {
        val results = popularListDTO.results
        if (results.isEmpty()) {
            onEmptyResponse()
        } else {
            onSuccessResponse(popularListDTO)
        }
    }

    private fun onSuccessResponse(popularListDTO: PopularListDTO) {
        putLoadResult(DETAILS_RESPONSE_SUCCESS_EXTRA)
        broadcastIntent.putExtra(DETAILS_TEMP_EXTRA, popularListDTO)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onMalformedURL() {
        putLoadResult(DETAILS_URL_MALFORMED_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onErrorRequest(error: String) {
        putLoadResult(DETAILS_REQUEST_ERROR_EXTRA)
        broadcastIntent.putExtra(DETAILS_REQUEST_ERROR_MESSAGE_EXTRA, error)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyResponse() {
        putLoadResult(DETAILS_RESPONSE_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyIntent() {
        putLoadResult(DETAILS_INTENT_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyData() {
        putLoadResult(DETAILS_DATA_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun putLoadResult(result: String) {
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, result)

    }
}