package com.kkk.moviereview.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.kkk.moviereview.R
import com.kkk.moviereview.network.networkresponse.Movie
import com.kkk.moviereview.ui.adapter.displayer.MovieListDisplayer
import com.kkk.moviereview.ui.adapter.displayer.TitleDisplayer
import com.kkk.moviereview.viewmodel.MainViewModel
import com.kkk.mylibrary.ui.activity.BaseActivity
import com.kkk.mylibrary.ui.adapter.DelegateAdapter
import com.kkk.mylibrary.ui.adapter.displayer.ItemDisplayer
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_main
    override val progressBarStyle: Int
        get() = com.kkk.mylibrary.R.style.ThemeLoadingDialog

    private val mViewModel: MainViewModel by viewModel()
    private var mItemList = mutableListOf<ItemDisplayer>()
    private var popularItemList = mutableListOf<ItemDisplayer>()
    private var upcomingItemList = mutableListOf<ItemDisplayer>()
    private val mAdapter: DelegateAdapter = DelegateAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rvMovieMain.apply {
                layoutManager = GridLayoutManager(applicationContext, 1)
                adapter = mAdapter
            }
        listenData()
        loadData()
        btnRetry.setOnClickListener {
            loadData()
        }

    }
    private fun listenData(){
        mViewModel.upcomingMovieSuccessState.observe(this) {
            upcomingItemList.clear()
            upcomingItemList.add(TitleDisplayer("Upcoming"))
            upcomingItemList.add(MovieListDisplayer(it, ::onClickItem, ::onClickFavourite))
            emptyView.visibility = View.GONE
            listenPopularSuccessData()
        }
        mViewModel.upcomingMovieErrorState.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            emptyView.visibility = if(mItemList.isEmpty()) View.VISIBLE else View.GONE
            hideLoadingView()
        })
        mViewModel.popularMovieErrorState.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            emptyView.visibility = if(mItemList.isEmpty()) View.VISIBLE else View.GONE
            hideLoadingView()
        })
    }

    private fun listenPopularSuccessData(){
        mViewModel.popularMovieSuccessState.observe(this) {
            popularItemList.clear()
            popularItemList.add(TitleDisplayer("Popular"))
            popularItemList.add(MovieListDisplayer(it, ::onClickItem, ::onClickFavourite))
            mItemList.clear()
            mItemList.addAll(upcomingItemList)
            mItemList.addAll(popularItemList)
                mAdapter.setData(mItemList)
                hideLoadingView()
        }
    }

    private fun loadData(){
        showLoadingView()
        mViewModel.loadUpcomingMovieList()
        mViewModel.loadPopularMovieList()
    }

    private fun onClickItem(data:Movie){
        val intent =
            data.movieId?.let {
                MovieDetailActivity.newIntent(this,data.iD,it,data.title!!,data.favouriteMovie!!,data.movieType!!)
            }
        startActivity(intent)
    }

    private fun onClickFavourite(data:Movie){
        val isFavourite = if (data.favouriteMovie == 0){
            1
        }else{
            0
        }
        mViewModel.updateFavoriteDataByMovieType(data.iD,isFavourite)
        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
}