package com.kkk.moviereview.data.db.dao

import androidx.room.*
import com.kkk.moviereview.network.networkresponse.Movie
import io.reactivex.Observable

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Movie>)

    @Query("select * from movie where movie_type=:movieType")
    fun allDataByMovieType(movieType:String): Observable<List<Movie>>

    @get:Query("select * from movie where is_favorite = 1 group by originalTitle")
    val allFavouriteData: Observable<List<Movie>>

    @Query("delete from movie where movie_type=:movieType")
    fun deleteAllDataByMovieType(movieType:String)

//    @Query("update movie set is_favorite=:isFavorite where iD=:iD and movie_type=:movieType")
//    fun updateFavoriteDataByMovieType(iD:Int,isFavorite: Int,movieType:String)

    @Query("update movie set is_favorite=:isFavorite where iD=:iD")
    fun updateFavoriteDataByMovieType(iD:Int,isFavorite: Int)


}