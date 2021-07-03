package ru.sorokin.flyfilmsx.view

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.annotation.RequiresApi
import ru.sorokin.flyfilmsx.R
import ru.sorokin.flyfilmsx.databinding.MainActivityBinding
import ru.sorokin.flyfilmsx.databinding.WebviewBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.getRoot()
        setContentView(view)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ListFragment.newInstance())
                    //.replace(R.id.container, ThreadsFragment.newInstance())
                    .commitNow()
        }
    }
}