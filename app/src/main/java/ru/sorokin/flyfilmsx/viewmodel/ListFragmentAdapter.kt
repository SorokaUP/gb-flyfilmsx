package ru.sorokin.flyfilmsx.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ru.sorokin.flyfilmsx.R
import ru.sorokin.flyfilmsx.databinding.ListFragmentBinding
import ru.sorokin.flyfilmsx.databinding.ListItemBinding
import ru.sorokin.flyfilmsx.model.Film
import ru.sorokin.flyfilmsx.view.ListFragment

class ListFragmentAdapter(private var onItemViewClickListener: ListFragment.OnItemViewClickListener?) :
    RecyclerView.Adapter<ListFragmentAdapter.ListViewHolder>() {

    private var filmData: List<Film> = listOf()

    inner class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(film: Film) {
            itemView.findViewById<TextView>(R.id.caption).text = film.caption
            itemView.findViewById<TextView>(R.id.tags).text = film.tags

            itemView.setOnClickListener {
                onItemViewClickListener?.onItemViewClick(film)
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