package ru.sorokin.flyfilmsx.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import ru.sorokin.flyfilmsx.databinding.MainActivityBinding
import ru.sorokin.flyfilmsx.databinding.WebviewBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    //private lateinit var binding: MainActivityBinding
    private lateinit var bindingWebview: WebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = MainActivityBinding.inflate(layoutInflater)
        //val view = binding.getRoot()
        //setContentView(view)

        bindingWebview = WebviewBinding.inflate(layoutInflater)
        bindingWebview.ok.setOnClickListener(clickListener)
        val view = bindingWebview.getRoot()
        setContentView(view)



        /*if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ListFragment.newInstance())
                    .commitNow()
        }*/
    }

    var clickListener: View.OnClickListener = object : View.OnClickListener {

        @RequiresApi(Build.VERSION_CODES.N)
        override fun onClick(v: View?) {
            try {
                val uri = URL(bindingWebview.url.text.toString())
                val handler = Handler() //Запоминаем основной поток
                Thread {
                    var urlConnection: HttpsURLConnection? = null
                    try {
                        urlConnection = uri.openConnection() as HttpsURLConnection
                        urlConnection.requestMethod = "GET" //установка метода получения данных — GET
                        urlConnection.readTimeout = 10000 //установка таймаута — 10 000 миллисекунд
                        val reader =
                            BufferedReader(InputStreamReader(urlConnection.inputStream)) //читаем данные в поток
                        val result = getLines(reader)

                        // Возвращаемся к основному потоку
                        handler.post {
                            //bindingWebview.webview.loadData(result, "text/html; charset=utf-8", "utf-8")
                            bindingWebview.webview.loadDataWithBaseURL(null, result, "text/html; charset=utf-8", "utf-8", null)
                        }
                    } catch (e: Exception) {
                        Log.e("", "Fail connection", e)
                        e.printStackTrace()
                    } finally {
                        urlConnection?.disconnect()
                    }
                }.start()
            } catch (e: MalformedURLException) {
                Log.e("", "Fail URI", e)
                e.printStackTrace()
            }
        }

        @RequiresApi(Build.VERSION_CODES.N)
        private fun getLines(reader: BufferedReader): String {
            return reader.lines().collect(Collectors.joining("\n"))
        }
    }
}