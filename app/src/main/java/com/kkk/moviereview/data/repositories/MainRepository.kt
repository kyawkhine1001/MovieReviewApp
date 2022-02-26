package com.kkk.moviereview.data.repositories

import androidx.lifecycle.MutableLiveData
import com.kkk.moviereview.network.networkresponse.*
import io.reactivex.Observable

interface MainRepository {
    var popularMovieData:MutableLiveData<Observable<MovieListResponse>>
    var upcomingMovieData:MutableLiveData<Observable<MovieListResponse>>
    var movieDetailData:MutableLiveData<Observable<MovieDetailResponse>>
    fun fetchPopularMovieData()
    fun fetchUpcomingMovieData()
    fun savePopularMovieDataIntoDatabase(movieList:List<Movie>)
    fun saveUpcomingMovieDataIntoDatabase(movieList:List<Movie>)
    fun updateFavoriteDataByMovieType(iD:Int,isFavorite: Int)
    fun fetchMovieDetail(movieId: Int)
}