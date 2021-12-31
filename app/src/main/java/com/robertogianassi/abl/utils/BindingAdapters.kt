package com.robertogianassi.abl.utils

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("android:src")
fun setImageViewResource(imageView: ImageView, resourceId: Int) {
    imageView.setImageResource(resourceId)
}

@BindingAdapter("android:backgroundTint")
fun setBackgroundTint(view: View, colorId: Int) {
    view.background.setTintList(ContextCompat.getColorStateList(view.context, colorId))
}
