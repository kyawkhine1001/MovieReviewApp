package com.kkk.moviereview.data.repositories

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.kkk.moviereview.data.db.MyDatabase
import com.kkk.moviereview.network.ApiService
import com.kkk.moviereview.network.networkresponse.*
import com.kkk.moviereview.util.AppConstants
import com.kkk.moviereview.util.MovieType
import com.kkk.mylibrary.network.rx.SchedulerProvider
import com.kkk.mylibrary.utils.SharedUtils
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainRepositoryImpl(
    private val context: Context,
    private val mApiService: ApiService,
    private val database: MyDatabase,
    private val schedulers: SchedulerProvider
) : MainRepository {
    override var popularMovieData: MutableLiveData<Observable<MovieListResponse>> = MutableLiveData()
    override var upcomingMovieData: MutableLiveData<Observable<MovieListResponse>> = MutableLiveData()
    override var movieDetailData: MutableLiveData<Observable<MovieDetailResponse>> = MutableLiveData()

    override fun fetchPopularMovieData() {
        val localMovieDataList = database.movieDao().allDataByMovieType(MovieType.POPULAR.title)
        val disposable = CompositeDisposable()
        disposable.add(
            localMovieDataList
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.io())
                .subscribe {
                    disposable.clear()
                    if (SharedUtils.isNetworkAvailable(context) && it.isEmpty()) {
                        popularMovieData.postValue(mApiService.loadMovieList(MovieType.POPULAR.title,AppConstants.apiKey))
                    } else {
                        val responseData = MovieListResponse()
                        responseData.results = it
                        popularMovieData.postValue(Observable.just(responseData))
                    }
                }
        )
    }
    override fun fetchUpcomingMovieData() {
        val localMovieDataList = database.movieDao().allDataByMovieType(MovieType.UPCOMING.title)
        val disposable = CompositeDisposable()
        disposable.add(
            localMovieDataList
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.io())
                .subscribe {
                    disposable.clear()
                    if (SharedUtils.isNetworkAvailable(context) && it.isEmpty()) {
                        upcomingMovieData.postValue(mApiService.loadMovieList(MovieType.UPCOMING.title,AppConstants.apiKey))
                    } else {
                        val responseData = MovieListResponse()
                        responseData.results = it
                        upcomingMovieData.postValue(Observable.just(responseData))
                    }
                }
        )
    }

    override fun savePopularMovieDataIntoDatabase(movieList: List<Movie>) {
        Observable.fromCallable {
            database.movieDao().deleteAllDataByMovieType(MovieType.POPULAR.title)
        }
            .doOnNext {
                movieList.map { it.movieType = MovieType.POPULAR.title }
                database.movieDao().insertAll(movieList)}
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.io())
            .subscribe()
    }

    override fun saveUpcomingMovieDataIntoDatabase(movieList: List<Movie>) {
        Observable.fromCallable{
            database.movieDao().deleteAllDataByMovieType(MovieType.UPCOMING.title)
            }
            .doOnNext {
                movieList.map { it.movieType =  MovieType.UPCOMING.title}
                database.movieDao().insertAll(movieList)}
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.io())
            .subscribe()
    }

    override fun updateFavoriteDataByMovieType(iD:Int,isFavorite: Int) {
        Observable.fromCallable{ database.movieDao().updateFavoriteDataByMovieType(iD,isFavorite)}
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.io())
            .subscribe()
    }

    override fun fetchMovieDetail(movieId: Int) {
        movieDetailData.postValue(mApiService.loadMovieDetail(movieId,AppConstants.apiKey))
    }
}