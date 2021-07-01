package ru.sorokin.flyfilmsx.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import ru.sorokin.flyfilmsx.databinding.FilmFragmentBinding
import ru.sorokin.flyfilmsx.model.Film
import ru.sorokin.flyfilmsx.model.FilmDTO
import ru.sorokin.flyfilmsx.viewmodel.FilmLoader
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

private const val API_KEY = "ad13319bfd35053445c0b0754f36eea2"
//Пример запроса: https://api.themoviedb.org/3/movie/550?api_key=ad13319bfd35053445c0b0754f36eea2

class FilmFragment : Fragment() {
    private var _binding: FilmFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var filmBundle: Film
    private val onLoadListener: FilmLoader.FilmLoaderListener =
        object : FilmLoader.FilmLoaderListener {

            override fun onLoaded(filmDTO: FilmDTO) {
                displayFilm(filmDTO)
            }

            override fun onFailed(throwable: Throwable) {
                //Обработка ошибки
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Так как с визуальными объектами проще всего взаимодействовать по имени, формируем
        // объект связку. Получает доступ к корневому элементу fragment_layout
        _binding = FilmFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Film>(ARG_FILM)?.let { film ->
            val loader = FilmLoader(onLoadListener, film.id)
            loader.loadFilm()
        }
        //filmBundle = arguments?.getParcelable(ARG_FILM) ?: Film()
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
    }

    private fun displayFilm(filmDTO: FilmDTO) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE

            filmCardCaption.text = filmDTO.original_title
            filmCardDescription.text = filmDTO.overview
            filmCardTags.text = filmDTO.genresToString()
            filmCardRate.rating = filmDTO.popularity ?: 0f
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_FILM = "film"

        @JvmStatic
        fun newInstance(film: Film) =
            FilmFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_FILM, film)
                }
            }
    }
}