package ru.sorokin.flyfilmsx.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.sorokin.flyfilmsx.R
import ru.sorokin.flyfilmsx.viewmodel.AppState
import ru.sorokin.flyfilmsx.model.Film
import ru.sorokin.flyfilmsx.databinding.ListFragmentBinding
import ru.sorokin.flyfilmsx.viewmodel.ListFragmentAdapter
import ru.sorokin.flyfilmsx.viewmodel.MainViewModel

class ListFragment : Fragment() {

    private var _binding: ListFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter = ListFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(film: Film) {
            activity?.supportFragmentManager?.let {
                it.beginTransaction()
                    .add(R.id.container, FilmFragment.newInstance(film))
                    .addToBackStack(null)
                    .commit()
            }
        }
    })

    companion object {
        fun newInstance() = ListFragment()
    }

    // Модель данных - поставщик, который будет хранить наши данные, будет объявлен позже, по
    // факту создания Fragment
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Так как с визуальными объектами проще всего взаимодействовать по имени, формируем
        // объект связку. Получает доступ к корневому элементу fragment_layout
        _binding = ListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        // Сообщаем фрагменту, о модели данных, с которой он будет общаться
        viewModel = ViewModelProvider(this).get(MainViewModel :: class.java)
        // Сразу же подписываемся на обновления всех данных от этой модели данных
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer<AppState> {
            // Действие, выполняемое по случаю обновления данных в поставщике
            renderData(it)
        })
        // И сразу же просим поставщика получить данные
        viewModel.getFilmsFromLocalSource()
    }

    private fun renderData(appState: AppState) {
        // В зависимости от того, чем сейчас занят поставщик, выполняем какие-то действия, о том
        // чем он занят нам сообщается из appState который в свою очередь будет одним из вариаций
        // Success(...), Loading(), Error(...)
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
                    .make(binding.root, getString(R.string.error_msg), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.reload_msg)) { viewModel.getFilmsFromLocalSource() }
                    .show()
            }
        }
    }

    private fun setData(filmData: List<Film>) {
        // TODO: Нужен RecyclerView
        if (filmData.isNotEmpty()) {
            adapter.setListFilm(filmData)
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter
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