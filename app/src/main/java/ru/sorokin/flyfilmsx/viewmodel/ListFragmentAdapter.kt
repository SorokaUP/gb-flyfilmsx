package ru.sorokin.flyfilmsx.viewmodel

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.sorokin.flyfilmsx.App.App.Companion.getHistoryDao
import ru.sorokin.flyfilmsx.R
import ru.sorokin.flyfilmsx.model.Film
import ru.sorokin.flyfilmsx.model.RestApiMethods
import ru.sorokin.flyfilmsx.room.DBRepository
import ru.sorokin.flyfilmsx.room.IDBRepository
import ru.sorokin.flyfilmsx.view.ListFragment

class ListFragmentAdapter(
    private var onItemViewClickListener: ListFragment.OnItemViewClickListener?,
    private val historyRepository: IDBRepository = DBRepository(getHistoryDao())
) :
    RecyclerView.Adapter<ListFragmentAdapter.ListViewHolder>() {

    private var filmData: List<Film> = listOf()

    inner class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(film: Film) {
            itemView.apply {
                findViewById<TextView>(R.id.caption).text = film.original_title
                findViewById<TextView>(R.id.content18plus).visibility = if (film.adult == true) { VISIBLE } else { INVISIBLE }
                val poster = findViewById<ImageView>(R.id.poster)

                context?.let {
                    val posterPath = RestApiMethods.ADDRESS_IMAGE_600X900 + film.poster_path
                    Glide.with(it)
                        .load(posterPath)
                        .into(poster)
                };

                setOnClickListener {
                    onItemViewClickListener?.onItemViewClick(film)
                }

                val likeInfo: List<Film> = historyRepository.getIsLike(film.id)
                findViewById<CheckBox>(R.id.is_like).let {
                    it.isChecked = if (likeInfo.isNotEmpty()) { likeInfo[0].isLike ?: false } else { false }
                    it.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (likeInfo.isNotEmpty()) {
                            historyRepository.setIsLike(film.id, if (isChecked) {1} else {0})
                        } else {
                            film.isLike = isChecked
                            historyRepository.saveEntity(film)
                        }
                    }
                }
            }
        }
    }

    fun setListFilm(data: List<Film>) {
        filmData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(filmData[position])
    }

    override fun getItemCount(): Int {
        return filmData.size
    }

    fun removeListener() {
        onItemViewClickListener = null
    }
}