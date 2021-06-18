package ru.sorokin.flyfilmsx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.sorokin.flyfilmsx.databinding.MainActivityBinding
import ru.sorokin.flyfilmsx.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(getLayoutInflater())
        val view = binding.getRoot()
        setContentView(view)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }
}