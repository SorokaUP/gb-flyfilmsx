package ru.sorokin.flyfilmsx.view

import android.app.SearchManager
import android.app.SearchManager.QUERY
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import ru.sorokin.flyfilmsx.R
import ru.sorokin.flyfilmsx.databinding.FragmentListBinding
import ru.sorokin.flyfilmsx.viewmodel.AppState
import ru.sorokin.flyfilmsx.model.Film
import ru.sorokin.flyfilmsx.viewmodel.ListFragmentAdapter
import ru.sorokin.flyfilmsx.viewmodel.MainViewModel

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val adapter = ListFragmentAdapter(object : OnItemViewClickListener {
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
        fun newInstance() = ListFragment()
    }

    // Модель данных - поставщик, который будет хранить наши данные, будет объявлен позже, по
    // факту создания Fragment
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Так как с визуальными объектами проще всего взаимодействовать по имени, формируем
        // объект связку. Получает доступ к корневому элементу fragment_layout
        _binding = FragmentListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
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
        viewModel.getFilmsFromServerSource()
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
                    { viewModel.getFilmsFromServerSource() }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val search = menu.findItem(R.id.menuSearch)

        val searchText = search.actionView as SearchView
        searchText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
                var isAdult: Boolean = false
                activity?.let {
                    isAdult = (it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_SHOW_18_PLUS, false))
                }
                viewModel.getFilmByNameLike(query, isAdult)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //searchUsers(newText.toLowerCase()) в разработке
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
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