package com.kkk.moviereview.ui.adapter.displayer

import android.view.View
import com.kkk.moviereview.R
import com.kkk.mylibrary.ui.adapter.displayer.ItemDisplayer
import com.kkk.mylibrary.ui.adapter.displayer.ViewType
import kotlinx.android.synthetic.main.item_title.view.*

class TitleDisplayer(private val title:String): ItemDisplayer {
    override fun getViewType(): ViewType = ViewType(R.layout.item_title)

    override fun bind(itemView: View) {
        itemView.apply { tvTitle.text = title }
    }

}