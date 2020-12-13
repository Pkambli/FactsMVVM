package com.example.factslistapplication.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.factslistapplication.R

fun ImageView.imageLoading(url: String) {
    Glide.with(this)
        .load(url)
        .fitCenter()
        .placeholder(R.drawable.defaultimage)
        .error(R.drawable.defaultimage)
        .into(this)

}