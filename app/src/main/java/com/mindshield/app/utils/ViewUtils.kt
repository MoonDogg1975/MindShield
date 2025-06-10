package com.mindshield.app.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

object ViewUtils {
    /**
     * Show a snackbar with the given message
     */
    fun showSnackbar(
        view: View,
        message: String,
        duration: Int = Snackbar.LENGTH_SHORT,
        actionText: String? = null,
        action: (() -> Unit)? = null
    ) {
        val snackbar = Snackbar.make(view, message, duration)
        
        actionText?.let { text ->
            action?.let { action ->
                snackbar.setAction(text) { action() }
            }
        }
        
        snackbar.show()
    }
    
    /**
     * Hide the soft keyboard
     */
    fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus
        view?.let {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
    
    /**
     * Show the soft keyboard
     */
    fun showKeyboard(activity: Activity, view: View) {
        view.requestFocus()
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
    
    /**
     * Get color from resources
     */
    fun getColor(context: Context, @ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(context, colorRes)
    }
    
    /**
     * Get drawable from resources
     */
    fun getDrawable(context: Context, @DrawableRes drawableRes: Int) {
        ContextCompat.getDrawable(context, drawableRes)
    }
    
    /**
     * Convert dp to pixels
     */
    fun dpToPx(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }
    
    /**
     * Convert pixels to dp
     */
    fun pxToDp(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }
    
    /**
     * Set click effect on a view
     */
    fun View.setClickEffect() {
        this.setOnTouchListener { v, event ->
            when (event.action) {
                android.view.MotionEvent.ACTION_DOWN -> {
                    v.alpha = 0.7f
                }
                android.view.MotionEvent.ACTION_UP, 
                android.view.MotionEvent.ACTION_CANCEL -> {
                    v.alpha = 1.0f
                }
            }
            false
        }
    }
}
