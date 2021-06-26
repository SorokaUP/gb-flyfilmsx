package ru.sorokin.flyfilmsx.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.sorokin.flyfilmsx.databinding.FragmentFilmBinding
import ru.sorokin.flyfilmsx.model.Film


class FilmFragment : Fragment() {
    private var _binding: FragmentFilmBinding? = null
    private val binding get() = _binding!!

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
        val film = arguments?.getParcelable<Film>(ARG_FILM)
        if (film != null) {
            val caption = film.caption
            binding.filmCardCaption.text = film.caption
            binding.filmCardDescription.text = film.description
            binding.filmCardTags.text = film.tags
            binding.filmCardRate.rating = film.rate
            val drawableResourceId =
                this.resources.getIdentifier(film.posterPath, "drawable", context?.packageName)
            binding.filmCardPoster.setImageDrawable(resources.getDrawable(drawableResourceId, null))
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