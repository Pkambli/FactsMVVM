package com.example.factslistapplication.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.factslistapplication.R

fun ImageView.imageLoading(url: String) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .placeholder(R.mipmap.ic_launcher)
        .error(R.mipmap.ic_launcher)
        .into(this)

}