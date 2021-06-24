package ru.sorokin.flyfilmsx.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import ru.sorokin.flyfilmsx.viewmodel.AppState
import ru.sorokin.flyfilmsx.model.Film
import ru.sorokin.flyfilmsx.databinding.MainFragmentBinding
import ru.sorokin.flyfilmsx.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    // Модель данных - поставщик, который будет хранить наши данные, будет объявлен позже, по
    // факту создания Fragment
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Так как с визуальными объектами проще всего взаимодействовать по имени, формируем
        // объект связку. Получает доступ к корневому элементу fragment_layout
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
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
                    .make(binding.mainView, "Error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getFilmsFromLocalSource() }
                    .show()
            }
        }
    }

    private fun setData(filmData: List<Film>) {
        // TODO: Нужен RecyclerView
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