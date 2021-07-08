package ru.sorokin.flyfilmsx.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.GsonBuilder
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.sorokin.flyfilmsx.BuildConfig
import java.util.*
import java.util.concurrent.TimeUnit

object RestApi {
    @RequiresApi(Build.VERSION_CODES.N)
    fun runQuery(requestLink: String, callback: Callback) {
        val builder: Request.Builder = Request.Builder().apply {
            url(requestLink)
        }
        OkHttpClient().newCall(builder.build()).enqueue(callback)
    }

    val api: IRestApiRetrofit = Retrofit.Builder()
        .baseUrl(RestApiMethods.ADDRESS)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .client(okHttpClient())
        .build().create(IRestApiRetrofit::class.java)

    private fun okHttpClient() : OkHttpClient {
        return OkHttpClient.Builder().let {
            it.connectTimeout(10, TimeUnit.SECONDS)
            it.readTimeout(10, TimeUnit.SECONDS)
            it.writeTimeout(10, TimeUnit.SECONDS)
            it.addInterceptor { chain ->
                val original: Request = chain.request()
                val requestBuilder: Request.Builder = original.newBuilder()
                    .url(
                        original.url().newBuilder()
                            .addQueryParameter("api_key", BuildConfig.THEMOVIEDB_API_KEY)
                            .addQueryParameter("language", Locale.getDefault().country)
                            .build())
                    .method(original.method(), original.body())
                val request: Request = requestBuilder.build()
                chain.proceed(request)
            }
        }.build()
    }
}