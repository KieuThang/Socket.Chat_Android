package com.github.kieuthang.login_chat.common.utils


import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation

object ViewPressEffectHelper {

    fun attach(view: View) {
        view.setOnTouchListener(ASetOnTouchListener(view))
    }

    private class ASetOnTouchListener internal constructor(v: View) : View.OnTouchListener {

        internal val ZERO_ALPHA = 1.0f
        internal val HALF_ALPHA = 0.6f
        internal val FIXED_DURATION = 100
        internal var alphaOrginally = 1.0f

        init {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                alphaOrginally = v.alpha
            }
        }

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                        val animation = AlphaAnimation(ZERO_ALPHA, HALF_ALPHA)
                        animation.duration = FIXED_DURATION.toLong()
                        animation.fillAfter = true
                        v.startAnimation(animation)
                    } else {
                        v.animate().setDuration(FIXED_DURATION.toLong()).alpha(HALF_ALPHA)
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                        val animation = AlphaAnimation(HALF_ALPHA, ZERO_ALPHA)
                        animation.duration = FIXED_DURATION.toLong()
                        animation.fillAfter = true
                        v.startAnimation(animation)
                    } else {
                        v.animate().setDuration(100).alpha(alphaOrginally)
                    }
                }
            }
            return false
        }

    }
}

