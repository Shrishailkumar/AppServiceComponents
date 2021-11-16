package com.android.cameracapturecomponent.util

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation


fun View.scaleInAnimation(duration: Long) {
    val fadeIn = ScaleAnimation(
        1f,
        0.5f,
        1f,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )
    fadeIn.duration = duration // animation duration in milliseconds

    fadeIn.fillAfter =
        true // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
    this.startAnimation(fadeIn)
}

fun View.scaleOutAnimation(duration: Long) {
    val fadeIn = ScaleAnimation(
        0.5f,
        1f,
        0.5f,
        1f,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )
    fadeIn.duration = duration // animation duration in milliseconds

    fadeIn.fillAfter =
        true // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
    this.startAnimation(fadeIn)
}

@SuppressLint("Recycle")
fun View.startColorBlinkAnimation() {
    val mAnim = AlphaAnimation(0.2f, 1.0f)
    mAnim.duration = 700; // Time of the blink
    mAnim.startOffset = 20;
    mAnim.repeatMode = Animation.REVERSE;
    mAnim.repeatCount = Animation.INFINITE;
    this.startAnimation(mAnim);
}

fun View.stopAnimation() {
    this.clearAnimation()
}