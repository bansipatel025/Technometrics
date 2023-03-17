package com.technometrics.data.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.technometrics.data.model.movie.ResultsItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addMovie(movie: ResultsItem)

    @Query("SELECT * FROM movie")
    fun getMovie(): Flow<List<ResultsItem>>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getMovieDetail(id: String): Flow<List<ResultsItem>>

    @Update
    fun updateMovie(movie: ResultsItem)

    @Delete
    fun deleteMovie(movie: ResultsItem)
}