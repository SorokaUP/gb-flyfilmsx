package ru.sorokin.flyfilmsx.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import ru.sorokin.flyfilmsx.R
import ru.sorokin.flyfilmsx.databinding.FragmentLikeBinding
import ru.sorokin.flyfilmsx.databinding.FragmentListBinding
import ru.sorokin.flyfilmsx.model.Film
import ru.sorokin.flyfilmsx.viewmodel.*

class LikeFragment : Fragment() {

    private var _binding: FragmentLikeBinding? = null
    private val binding get() = _binding!!
    private val adapter = LikeFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(film: Film) {
            activity?.supportFragmentManager?.let {
                it.beginTransaction()
                    .add(R.id.fragment_container, FilmFragment.newInstance(film))
                    .addToBackStack(null)
                    .commit()
            }
        }
    })

    companion object {
        fun newInstance() = LikeFragment()
    }

    // Модель данных - поставщик, который будет хранить наши данные, будет объявлен позже, по
    // факту создания Fragment
    private val viewModel: LikeViewModel by lazy {
        ViewModelProvider(this).get(LikeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Так как с визуальными объектами проще всего взаимодействовать по имени, формируем
        // объект связку. Получает доступ к корневому элементу fragment_layout
        _binding = FragmentLikeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        // Сообщаем фрагменту, о модели данных, с которой он будет общаться
        // Сразу же подписываемся на обновления всех данных от этой модели данных
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer<AppState> {
            // Действие, выполняемое по случаю обновления данных в поставщике
            renderData(it)
        })
        // И сразу же просим поставщика получить данные
        viewModel.getFilmsLikeFromDataBase()
    }

    private fun renderData(appState: AppState) {
        // В зависимости от того, чем сейчас занят поставщик, выполняем какие-то действия, о том
        // чем он занят нам сообщается из appState который в свою очередь будет одним из вариаций
        // Success(...), Loading(), Error(...)
        when (appState) {
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                setData(appState.filmData)
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                binding.root.showSnackBar(
                    getString(R.string.error_msg),
                    getString(R.string.reload_msg),
                    { viewModel.getFilmsLikeFromDataBase() }
                )
            }
        }
    }

    private fun setData(filmData: List<Film>) {
        filmData?.let {
            adapter.setListFilm(filmData)
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter
    }

    private fun View.showSnackBar(
        text: String,
        actionText: String,
        action: (View) -> Unit,
        length: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(this, text, length).setAction(actionText, action).show()
    }

    private fun View.showSnackBar(
        stringId: Int,
        length: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(this, getString(stringId), length).show()
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(film: Film)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.removeListener()
        _binding = null
    }
}