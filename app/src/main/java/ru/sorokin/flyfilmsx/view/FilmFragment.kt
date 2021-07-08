package ru.sorokin.flyfilmsx.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.sorokin.flyfilmsx.R
import ru.sorokin.flyfilmsx.databinding.FilmFragmentBinding
import ru.sorokin.flyfilmsx.model.*
import ru.sorokin.flyfilmsx.viewmodel.AppState
import kotlin.properties.Delegates

class FilmFragment : Fragment() {
    private var _binding: FilmFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var filmBundle: Film
    private var filmId: Int = 0
    private val callBack = object :
        Callback<FilmDTO> {

        override fun onResponse(call: Call<FilmDTO>, response: Response<FilmDTO>) {
            val filmDTO: FilmDTO? = response.body()
            if (response.isSuccessful && filmDTO != null) {
                displayFilm(filmDTO)
            } else {
                binding.root.showSnackBar(
                    getString(R.string.error_msg),
                    getString(R.string.reload_msg),
                    { RestApi.api.getFilm(filmId).enqueue(this) }
                )
            }
        }

        override fun onFailure(call: Call<FilmDTO>, t: Throwable) {
            AppState.Error(t)
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
        arguments?.getParcelable<FilmDTO>(ARG_FILM)?.let { film ->
            filmId = film.id
            RestApi.api.getFilm(filmId).enqueue(callBack)
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
        fun newInstance(film: FilmDTO) =
            FilmFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_FILM, film)
                }
            }
    }
}