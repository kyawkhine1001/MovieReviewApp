package com.kkk.moviereview.network

import com.kkk.moviereview.network.networkresponse.MovieDetailResponse
import com.kkk.moviereview.network.networkresponse.MovieListResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/{name}")
    fun loadMovieList(@Path("name") name: String, @Query("api_key") apiKey:String): Observable<MovieListResponse>

    @GET("movie/{movie_id}")
    fun loadMovieDetail(@Path("movie_id") movie_id: Int, @Query("api_key") apiKey:String): Observable<MovieDetailResponse>
}