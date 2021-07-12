package ru.sorokin.flyfilmsx.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.sorokin.flyfilmsx.R
import ru.sorokin.flyfilmsx.databinding.FragmentFilmBinding
import ru.sorokin.flyfilmsx.model.*
import ru.sorokin.flyfilmsx.viewmodel.AppState
import ru.sorokin.flyfilmsx.viewmodel.FilmViewModel
import ru.sorokin.flyfilmsx.viewmodel.MainViewModel

class FilmFragment : Fragment() {
    private var _binding: FragmentFilmBinding? = null
    private val binding get() = _binding!!
    private var filmId: Int = 0
    private lateinit var film: Film
    private val callBack = object :
        Callback<Film> {

        override fun onResponse(call: Call<Film>, response: Response<Film>) {
            val film: Film? = response.body()
            if (response.isSuccessful && film != null) {
                setFilm(film)
                displayFilm(film)
                viewModel.getComment(film.id)
            } else {
                binding.root.showSnackBar(
                    getString(R.string.error_msg),
                    getString(R.string.reload_msg),
                    { RestApi.api.getFilm(filmId).enqueue(this) }
                )
            }
        }

        override fun onFailure(call: Call<Film>, t: Throwable) {
            AppState.Error(t)
        }
    }

    private fun setFilm(film: Film) {
        this.film = film
    }

    private val viewModel: FilmViewModel by lazy {
        ViewModelProvider(this).get(FilmViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Так как с визуальными объектами проще всего взаимодействовать по имени, формируем
        // объект связку. Получает доступ к корневому элементу fragment_layout
        _binding = FragmentFilmBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Film>(ARG_FILM)?.let { film ->
            filmId = film.id
            RestApi.api.getFilm(filmId).enqueue(callBack)
        }
        //filmBundle = arguments?.getParcelable(ARG_FILM) ?: Film()
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE

        binding.btnSaveComment.setOnClickListener {
            viewModel.setComment(this.filmId, binding.txtComment.text.toString())
            Toast.makeText(context, "Сохранено", Toast.LENGTH_SHORT).show()
        }

        // Сообщаем фрагменту, о модели данных, с которой он будет общаться
        // Сразу же подписываемся на обновления всех данных от этой модели данных
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer<AppState> {
            // Действие, выполняемое по случаю обновления данных в поставщике
            appendFromDB(it)
        })
    }

    private fun appendFromDB(appState: AppState) {
        when(appState) {
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                this.film.comment = appState.filmData[0].comment
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
            }
        }
    }

    private fun displayFilm(film: Film) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE

            filmCardCaption.text = film.original_title
            filmCardDescription.text = film.overview
            filmCardTags.text = film.genresToString()
            filmCardRate.rating = film.popularity ?: 0f

            context?.let {
                val posterPath = RestApiMethods.ADDRESS_IMAGE_600X900 + film.poster_path
                Glide.with(it)
                    .load(posterPath)
                    .into(filmCardPoster)
            };
        }
    }

    private fun View.showSnackBar(
        text: String,
        actionText: String,
        action: (View) -> Unit,
        length: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(this, text, length).setAction(actionText, action).show()
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