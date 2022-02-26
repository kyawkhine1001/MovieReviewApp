package com.kkk.moviereview.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kkk.moviereview.data.repositories.MainRepository
import com.kkk.moviereview.network.networkresponse.*
import com.kkk.moviereview.network.networkresponse.Movie
import com.kkk.moviereview.network.networkresponse.MovieDetailResponse
import com.kkk.mylibrary.network.rx.SchedulerProvider
import com.kkk.mylibrary.viewmodel.BaseViewModel

class MainViewModel(
    private val mainRepo: MainRepository,
    private val schedulers: SchedulerProvider
) : BaseViewModel() {
    var upcomingMovieErrorState = MutableLiveData<String>()
    var upcomingMovieSuccessState = MutableLiveData<List<Movie>>()

    var popularMovieErrorState = MutableLiveData<String>()
    var popularMovieSuccessState = MutableLiveData<List<Movie>>()

    var movieDetailErrorState = MutableLiveData<String>()
    var movieDetailSuccessState = MutableLiveData<MovieDetailResponse>()

    fun loadUpcomingMovieList() {
        mainRepo.upcomingMovieData
            .observeForever {
                launch {
                    it
                        .subscribeOn(schedulers.io())
                        .observeOn(schedulers.mainThread())
                        .doOnNext {
                            it.results?.let { it1 -> mainRepo.saveUpcomingMovieDataIntoDatabase(it1) }
                        }
                        .subscribe({ response ->
                            mainRepo.upcomingMovieData = MutableLiveData()
                            upcomingMovieSuccessState.postValue(response.results)
                        }, { error ->
                            upcomingMovieErrorState.postValue(error.localizedMessage)
                        })
                }
            }
        mainRepo.fetchUpcomingMovieData()
    }

    fun loadPopularMovieList() {
        mainRepo.popularMovieData
            .observeForever {
                launch {
                    it
                        .subscribeOn(schedulers.io())
                        .observeOn(schedulers.mainThread())
                        .doOnNext {
                            it.results?.let { it1 -> mainRepo.savePopularMovieDataIntoDatabase(it1) }
                        }
                        .subscribe({ response ->
                            mainRepo.popularMovieData = MutableLiveData()
                            popularMovieSuccessState.postValue(response.results)
                        }, { error ->
                            popularMovieErrorState.postValue(error.localizedMessage)
                        })
                }
            }


        mainRepo.fetchPopularMovieData()
    }

    fun updateFavoriteDataByMovieType(id:Int,isFavorite: Int) {
        mainRepo.updateFavoriteDataByMovieType(id,isFavorite)
    }

    fun loadMovieDetail(movieId:Int){
        mainRepo.movieDetailData
            .observeForever {
                launch {
                    it
                        .subscribeOn(schedulers.io())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ response ->
                            mainRepo.movieDetailData = MutableLiveData()
                            movieDetailSuccessState.postValue(response)
                        }, { error ->
                            movieDetailErrorState.postValue(error.localizedMessage)
                        })
                }
            }
        mainRepo.fetchMovieDetail(movieId)
    }
}
