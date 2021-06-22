package ru.sorokin.flyfilmsx.model

import java.io.Serializable
import java.util.*

data class Film(
    val id: Int,
    val caption: String,
    val description: String,
    val dateFrom: Date,
    val tags: String,
    val posterPath: String
    ) : Serializable
