package ru.sorokin.flyfilmsx.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.sorokin.flyfilmsx.R
import ru.sorokin.flyfilmsx.model.Film

class FilmFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_film, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(film: Film) =
            FilmFragment().apply {
            }
    }
}