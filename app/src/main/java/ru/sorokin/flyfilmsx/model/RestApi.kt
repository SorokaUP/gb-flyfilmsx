package ru.sorokin.flyfilmsx.model

import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.*

object RestApi {
    @RequiresApi(Build.VERSION_CODES.N)
    fun runQuery(requestLink: String, callback: Callback) {
        val builder: Request.Builder = Request.Builder().apply {
            url(requestLink)
        }
        OkHttpClient().newCall(builder.build()).enqueue(callback)
    }
}