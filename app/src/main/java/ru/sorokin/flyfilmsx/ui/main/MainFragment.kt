package ru.sorokin.flyfilmsx.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import ru.sorokin.flyfilmsx.AppState
import ru.sorokin.flyfilmsx.Film
import ru.sorokin.flyfilmsx.R
import ru.sorokin.flyfilmsx.databinding.MainActivityBinding
import ru.sorokin.flyfilmsx.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel :: class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer<AppState> {
            renderData(it)
        })
        viewModel.getFilms()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val filmData = appState.filmData
                binding.loadingLayout.visibility = View.GONE
                setData(filmData)
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.mainView, "Error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getFilms() }
                    .show()
            }
        }
    }

    private fun setData(filmData: List<Film>) {
        if (filmData.isNotEmpty()) {
            binding.filmCaption.text = filmData[0].caption
            binding.filmDescription.text = filmData[0].description
            binding.filmTags.text = filmData[0].tags
            binding.filmDateFrom.text = filmData[0].dateFrom.toString()
            //binding.filmPoster.setImageURI(filmData[0].posterPath)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}