package ru.sorokin.flyfilmsx.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.sorokin.flyfilmsx.R
import ru.sorokin.flyfilmsx.databinding.FragmentFilmBinding
import ru.sorokin.flyfilmsx.model.*
import ru.sorokin.flyfilmsx.viewmodel.AppState

class FilmFragment : Fragment() {
    private var _binding: FragmentFilmBinding? = null
    private val binding get() = _binding!!
    private var filmId: Int = 0
    private val callBack = object :
        Callback<Film> {

        override fun onResponse(call: Call<Film>, response: Response<Film>) {
            val film: Film? = response.body()
            if (response.isSuccessful && film != null) {
                displayFilm(film)
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