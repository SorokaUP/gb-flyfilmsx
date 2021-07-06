package ru.sorokin.flyfilmsx.model

import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection
import kotlin.concurrent.thread

object RestApi {
    interface IRestApiListener {
        fun onLoaded(res: String)
        fun onFailed(throwable: Throwable)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun runQuery(query: String, listener: IRestApiListener) {
        try {
            val uri = URL(query)
            lateinit var urlConnection: HttpsURLConnection
            val handler = Handler()
            Thread(Runnable {
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    val res = getFromUrl(urlConnection)
                    handler.post { listener.onLoaded(res) }
                } catch (e: Exception) {
                    onException(e, "Fail connection")
                    listener.onFailed(e)
                } finally {
                    urlConnection.disconnect()
                }
            }).start()
        } catch (e: MalformedURLException) {
            onException(e, "Fail URI")
            listener.onFailed(e)
        }
    }

    fun runQueryWithOutThread(query: String, isNeedException: Boolean = false) : String {
        var res = ""
        var ex = Exception("")
        val uri = URL(query)
        lateinit var urlConnection: HttpsURLConnection
        try {
            urlConnection = uri.openConnection() as HttpsURLConnection
            res = getFromUrl(urlConnection)
        } catch (e: Exception) {
            onException(e, "Fail connection")
            ex = e
        } finally {
            urlConnection.disconnect()
            if (ex.message != "" && isNeedException)
                throw ex
            return res
        }
    }

    private fun getFromUrl(urlConnection: HttpsURLConnection) : String {
        urlConnection.requestMethod = "GET"
        urlConnection.readTimeout = 10000
        val bufferedReader =
            BufferedReader(InputStreamReader(urlConnection.inputStream))
        return getLines(bufferedReader)
    }

    private fun onException(e: Throwable, msg: String) {
        Log.e("", msg, e)
        e.printStackTrace()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }
}