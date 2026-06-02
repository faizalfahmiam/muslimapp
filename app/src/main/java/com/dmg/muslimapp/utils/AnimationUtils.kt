package com.dmg.muslimapp.utils

import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.TranslateAnimation
import com.dmg.muslimapp.R

/**
 * For PT. DIGITAL MUSLIM GLOBAL
 * Created by Ridwan Ismail on 11 October 2017
 * You can contact me at : ismail.ridwan98@gmail.com
 * -------------------------------------------------
 * MUSLIM APP
 * com.dmg.muslimapp.utils
 * or see link for more detail https://gitlab.com/iwanz98/muslim-app
 */

object AnimationUtils {

    /**
     * Animates a view so that it slides in from the left of it's container.
     *
     * @param context
     * @param view
     */
    fun slideInFromLeft(context: Context, view: View) {
        runSimpleAnimation(context, view, R.anim.slide_from_left)
    }

    /**
     * Animates a view so that it slides from its current position, out of view to the left.
     *
     * @param context
     * @param view
     */
    fun slideOutToLeft(context: Context, view: View) {
        runSimpleAnimation(context, view, R.anim.slide_to_left)
    }

    /**
     * Animates a view so that it slides in the from the right of it's container.
     *
     * @param context
     * @param view
     */
    fun slideInFromRight(context: Context, view: View) {
        runSimpleAnimation(context, view, R.anim.slide_from_right)
    }

    /**
     * Animates a view so that it slides from its current position, out of view to the right.
     *
     * @param context
     * @param view
     */
    fun slideOutToRight(context: Context, view: View) {
        runSimpleAnimation(context, view, R.anim.slide_to_right)
    }

    /**
     * Runs a simple animation on a View with no extra parameters.
     *
     * @param context
     * @param view
     * @param animationId
     */
    private fun runSimpleAnimation(context: Context, view: View, animationId: Int) {
        view.startAnimation(android.view.animation.AnimationUtils.loadAnimation(
                context, animationId
        ))
    }

    /**
     * Set alpha animation
     *
     * @param v
     * @param duration
     * @param visibility
     */
    fun startAlphaAnimation(v: View, duration: Long, visibility: Int) {
        val alphaAnimation = if (visibility == View.VISIBLE)
            AlphaAnimation(0f, 1f)
        else
            AlphaAnimation(1f, 0f)

        alphaAnimation.duration = duration
        alphaAnimation.fillAfter = true
        v.startAnimation(alphaAnimation)
    }

    // To animate view slide out from left to right
    fun slideToRight(view: View) {
        val animate = TranslateAnimation(0f, view.width.toFloat(), 0f, 0f)
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
        view.visibility = View.GONE
    }

    // To animate view slide out from right to left
    fun slideToLeft(view: View) {
        val animate = TranslateAnimation(0f, (-view.width).toFloat(), 0f, 0f)
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
        view.visibility = View.GONE
    }

    // To animate view slide out from top to bottom
    fun slideToBottom(view: View, visibility: Int) {
        val animate = TranslateAnimation(0f, 0f, 0f, view.height.toFloat())
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
        view.visibility = visibility
    }

    // To animate view slide out from bottom to top
    fun slideToTop(view: View, visibility: Int) {
        val animate = TranslateAnimation(0f, 0f, 0f, (-view.height).toFloat())
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
        view.visibility = visibility
    }
}
