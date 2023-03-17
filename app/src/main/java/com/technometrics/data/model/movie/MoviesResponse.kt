package com.technometrics.data.model.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.technometrics.data.api.RestConstant

data class MoviesResponse(

    @field:SerializedName("page")
    val page: Int? = null,

    @field:SerializedName("total_pages")
    val totalPages: Int? = null,

    @field:SerializedName("results")
    val results: List<ResultsItem>? = null,

    @field:SerializedName("total_results")
    val totalResults: Int? = null
)

@Entity(tableName = RestConstant.TableName)
data class ResultsItem(

    @ColumnInfo(name = "overview")
    @field:SerializedName("overview")
    val overview: String? = null,

    @ColumnInfo(name = "original_language")
    @field:SerializedName("original_language")
    val originalLanguage: String? = null,

    @ColumnInfo(name = "original_title")
    @field:SerializedName("original_title")
    val originalTitle: String? = null,

    @ColumnInfo(name = "video")
    @field:SerializedName("video")
    val video: Boolean? = null,

    @PrimaryKey
    @ColumnInfo(name = "title")
    @field:SerializedName("title")
    val title: String,

    @ColumnInfo(name = "genre_ids")
    @field:SerializedName("genre_ids")
    val genreIds: List<Int?>? = null,

    @ColumnInfo(name = "poster_path")
    @field:SerializedName("poster_path")
    val posterPath: String? = null,

    @ColumnInfo(name = "backdrop_path")
    @field:SerializedName("backdrop_path")
    val backdropPath: String? = null,

    @ColumnInfo(name = "release_date")
    @field:SerializedName("release_date")
    val releaseDate: String? = null,

    @ColumnInfo(name = "popularity")
    @field:SerializedName("popularity")
    val popularity: Double? = null,

    @ColumnInfo(name = "vote_average")
    @field:SerializedName("vote_average")
    val voteAverage: String? = null,

    @ColumnInfo(name = "id")
    @field:SerializedName("id")
    val id: Int? = null,

    @ColumnInfo(name = "adult")
    @field:SerializedName("adult")
    val adult: Boolean? = null,

    @ColumnInfo(name = "vote_count")
    @field:SerializedName("vote_count")
    val voteCount: Int? = null
)
