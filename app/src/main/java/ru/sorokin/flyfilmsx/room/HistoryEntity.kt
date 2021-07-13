package ru.sorokin.flyfilmsx.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val film_id: Int,
    val original_title: String,
    val poster_path: String,
    val is_like: Int,
    val comment: String
)