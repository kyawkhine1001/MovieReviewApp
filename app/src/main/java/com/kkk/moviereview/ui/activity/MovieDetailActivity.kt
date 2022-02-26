package com.kkk.moviereview.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kkk.moviereview.R
import com.kkk.moviereview.network.networkresponse.MovieDetailResponse
import com.kkk.moviereview.util.AppConstants
import com.kkk.moviereview.viewmodel.MainViewModel
import com.kkk.mylibrary.ui.activity.BaseActivity
import com.kkk.mylibrary.utils.SharedUtils
import com.kkk.mylibrary.utils.loadImageWithGlide
import kotlinx.android.synthetic.main.activity_movie_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_movie_detail
    override val progressBarStyle: Int
        get() = com.kkk.mylibrary.R.style.ThemeLoadingDialog

    var iD:Int? = null
    var movieId:Int? = null
    var isFavouriteMovie:Boolean = false
    var movieType:String? = ""

    private val mViewModel: MainViewModel by viewModel()

    companion object{
        private const val IE_NAME = "IE_NAME"
        private const val IE_ID = "IE_ID"
        private const val IE_MOVIE_ID = "IE_MOVIE_ID"
        private const val IE_FAVORITE_MOVIE = "IE_FAVORITE_MOVIE"
        private const val IE_MOVIE_TYPE = "IE_MOVIE_TYPE"
        fun newIntent(
            context: Context,
            iD: Int,
            movieId: Int,
            movieName: String,
            favouriteMovie: Int,
            movieType: String
        ): Intent {
            val intent = Intent(context,MovieDetailActivity::class.java)
            intent.putExtra(IE_NAME,movieName)
            intent.putExtra(IE_MOVIE_ID,movieId)
            intent.putExtra(IE_ID,iD)
            intent.putExtra(IE_FAVORITE_MOVIE,favouriteMovie)
            intent.putExtra(IE_MOVIE_TYPE,movieType)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(IE_NAME)
        iD = intent.getIntExtra(IE_ID,0)
        movieId = intent.getIntExtra(IE_MOVIE_ID,0)
        isFavouriteMovie = intent.getIntExtra(IE_FAVORITE_MOVIE,0) == 1
        movieType = intent.getStringExtra(IE_MOVIE_TYPE)
        nsMovieDetail.visibility = View.INVISIBLE
        showLoadingView()
        mViewModel.loadMovieDetail(movieId!!)
        mViewModel.movieDetailSuccessState.observe(this, Observer {
            bindDetailView(it)
            nsMovieDetail.visibility = View.VISIBLE
            emptyViewDetail.visibility = View.GONE
            hideLoadingView()
        })

        mViewModel.movieDetailErrorState.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            emptyViewDetail.visibility = View.VISIBLE
            hideLoadingView()
        })
        btnRetry.setOnClickListener { mViewModel.loadMovieDetail(movieId!!) }
    }

    private fun bindDetailView(movieData: MovieDetailResponse) {
        movieData.backdropPath?.let {
            val url = AppConstants.imageBaseUrl+it
            ivMovieDetail.loadImageWithGlide(this,url)
        }
        val runTime: Pair<String, String> = SharedUtils.formatHoursAndMinutes(movieData.runtime!!)
        val runTimeString = "${runTime.first}h ${runTime.second}m"
        tvDetailTitle.text =  movieData.originalTitle
        tvDetailReleaseDate.text = "${movieData.releaseDate} | $runTimeString"
        tvDetailOverview.text = movieData.overview
        var genreString = ""
        movieData.genres?.forEachIndexed { index, genre ->
            genreString+=genre.name
            genreString+=if (index==movieData.genres!!.size-1) "" else ", "
        }
        tvDetailGenre.text = genreString
        var productionString = ""
        movieData.productionCompanies?.forEachIndexed { index, company ->
            productionString+=company.name
            productionString+=if (index==movieData.productionCompanies!!.size-1) "" else ", "
        }
        tvDetailProduction.text = productionString
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.movie_detail_menu, menu)
        val favouriteMenu = menu?.findItem(R.id.menu_favourite)
        if (isFavouriteMovie){
            favouriteMenu?.setIcon(android.R.drawable.star_on)
        }else{
            favouriteMenu?.setIcon(android.R.drawable.star_off)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.menu_favourite) {
            var isFavourite = 0
            if (isFavouriteMovie){
                isFavourite = 0
                item.setIcon(android.R.drawable.star_off)
            }else{
                isFavourite = 1
                item.setIcon(android.R.drawable.star_on)
            }
            mViewModel.updateFavoriteDataByMovieType(iD!!,isFavourite)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}