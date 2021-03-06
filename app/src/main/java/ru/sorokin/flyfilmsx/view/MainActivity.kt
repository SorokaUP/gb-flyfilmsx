package ru.sorokin.flyfilmsx.view

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import ru.sorokin.flyfilmsx.R
import ru.sorokin.flyfilmsx.databinding.AppBarMainBinding
import ru.sorokin.flyfilmsx.databinding.MainActivityBinding
import ru.sorokin.flyfilmsx.viewmodel.MainBroadcastReceiver

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    private lateinit var bindingToolbar: AppBarMainBinding
    private val receiver = MainBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        bindingToolbar = AppBarMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        registerReceiver(receiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            goToMain()
            //goToContacts()
        }
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return if (menu != null) {
            val search: MenuItem = menu.findItem(R.id.menuSearch)
            val searchText: SearchView = search.actionView as SearchView

            searchText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    Toast.makeText(binding.root.context, query, Toast.LENGTH_LONG).show()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
            true
        } else {
            super.onCreateOptionsMenu(menu)
        }
    }*/

    private fun goToMain() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ListFragment.newInstance())
            .commitNow()
    }

    private fun goToContacts() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ContactsFragment.newInstance())
            .commitNow()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menuMain -> {
                goToMain()
                true
            }
            R.id.menuSettings -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SettingsFragment.newInstance())
                    .addToBackStack(null)
                    .commit()

                true
            }
            R.id.menuLike -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, LikeFragment.newInstance())
                    .addToBackStack(null)
                    .commit()

                true
            }
            R.id.menu_google_maps -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .add(R.id.container, MapsFragment())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }

                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}