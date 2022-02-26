package com.kkk.moviereview.ui.adapter.displayer

import android.view.View
import com.kkk.moviereview.util.AppConstants
import com.kkk.moviereview.R
import com.kkk.moviereview.network.networkresponse.Movie
import com.kkk.mylibrary.ui.adapter.displayer.ItemDisplayer
import com.kkk.mylibrary.ui.adapter.displayer.ViewType
import com.kkk.mylibrary.utils.loadImageWithGlide
import kotlinx.android.synthetic.main.item_movie_item.view.*

class MovieItemDisplayer(val data: Movie, val onClick:(Movie) -> Unit,private val onClickFavorite:(Movie) -> Unit): ItemDisplayer {
    override fun getViewType(): ViewType = ViewType(R.layout.item_movie_item)

    override fun bind(itemView: View) {
        itemView.apply {
            data.posterPath?.let {
                val url = AppConstants.imageBaseUrl+it
                ivMovie.loadImageWithGlide(itemView.context,url)
            }
            tvMovieTitle.text = if(data.title==null) data.title else data.originalTitle
            tvMovieReleaseDate.text = data.releaseDate
            if (data.favouriteMovie == 0){
                ivFavourite.setImageResource(android.R.drawable.star_off)
            }else{
                ivFavourite.setImageResource(android.R.drawable.star_on)
            }
            ivFavourite.setOnClickListener {
//                if (data.favouriteMovie == 0){
//                    ivFavourite.setImageResource(android.R.drawable.star_on)
//                }else{
//                    ivFavourite.setImageResource(android.R.drawable.star_off)
//                }
                onClickFavorite(data)
            }
            setOnClickListener { onClick(data) }
        }
    }

}