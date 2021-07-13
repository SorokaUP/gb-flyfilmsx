package ru.sorokin.flyfilmsx.room

import androidx.room.*

@Dao
interface HistoryDao {

    @Query("SELECT * FROM HistoryEntity")
    fun all(): List<HistoryEntity>

    @Query("SELECT * FROM HistoryEntity WHERE original_title LIKE :original_title")
    fun getDataByWord(original_title: String): List<HistoryEntity>

    @Query("SELECT * FROM HistoryEntity WHERE is_like = 1")
    fun getDataLike(): List<HistoryEntity>

    @Query("SELECT * FROM HistoryEntity WHERE film_id = :film_id")
    fun getDataLike(film_id: Int): List<HistoryEntity>

    @Query("select * from HistoryEntity where film_id = :film_id")
    fun getFilmByFilmId(film_id: Int) : List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HistoryEntity)

    @Update
    fun update(entity: HistoryEntity)

    @Delete
    fun delete(entity: HistoryEntity)

    @Query("update HistoryEntity set is_like = :is_like where film_id = :film_id")
    fun setIsLike(film_id: Int, is_like: Int)

    @Query("select * from HistoryEntity where film_id = :film_id")
    fun getIsLike(film_id: Int): List<HistoryEntity>

    @Query("update HistoryEntity set comment = :comment where film_id = :film_id")
    fun setComment(film_id: Int, comment: String)

    @Query("select comment from HistoryEntity where film_id = :film_id")
    fun getComment(film_id: Int): String

    @Query("select * from HistoryEntity where original_title like :title+'%'")
    fun getFilmByNameLike(title: String) : List<HistoryEntity>
}