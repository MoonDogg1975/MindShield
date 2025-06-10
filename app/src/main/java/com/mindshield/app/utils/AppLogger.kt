package com.mindshield.app.utils

import android.util.Log

/**
 * A simple logging utility class that wraps the Android Log class.
 * This allows for easy enabling/disabling of logs and adding a global tag.
 */
object AppLogger {
    // Enable/disable logging for different log levels
    var isDebugEnabled = BuildConfig.DEBUG
    var isInfoEnabled = true
    var isWarningEnabled = true
    var isErrorEnabled = true
    
    // Default tag
    private const val DEFAULT_TAG = "MindShield"
    
    // Log levels
    private const val VERBOSE = 0
    private const val DEBUG = 1
    private const val INFO = 2
    private const val WARN = 3
    private const val ERROR = 4
    
    /**
     * Log a verbose message
     */
    fun v(tag: String = DEFAULT_TAG, message: String) {
        if (isDebugEnabled) {
            log(VERBOSE, tag, message)
        }
    }
    
    /**
     * Log a debug message
     */
    fun d(tag: String = DEFAULT_TAG, message: String) {
        if (isDebugEnabled) {
            log(DEBUG, tag, message)
        }
    }
    
    /**
     * Log an info message
     */
    fun i(tag: String = DEFAULT_TAG, message: String) {
        if (isInfoEnabled) {
            log(INFO, tag, message)
        }
    }
    
    /**
     * Log a warning message
     */
    fun w(tag: String = DEFAULT_TAG, message: String) {
        if (isWarningEnabled) {
            log(WARN, tag, message)
        }
    }
    
    /**
     * Log an error message
     */
    fun e(tag: String = DEFAULT_TAG, message: String, throwable: Throwable? = null) {
        if (isErrorEnabled) {
            if (throwable != null) {
                log(ERROR, tag, "$message\n${Log.getStackTraceString(throwable)}")
            } else {
                log(ERROR, tag, message)
            }
        }
    }
    
    /**
     * Log a debug message with the class name as the tag
     */
    fun debug(message: String) {
        if (isDebugEnabled) {
            val className = Thread.currentThread().stackTrace[3].className
            val simpleClassName = className.substring(className.lastIndexOf('.') + 1)
            log(DEBUG, simpleClassName, message)
        }
    }
    
    /**
     * Log an error message with the class name as the tag
     */
    fun error(message: String, throwable: Throwable? = null) {
        if (isErrorEnabled) {
            val className = Thread.currentThread().stackTrace[3].className
            val simpleClassName = className.substring(className.lastIndexOf('.') + 1)
            if (throwable != null) {
                log(ERROR, simpleClassName, "$message\n${Log.getStackTraceString(throwable)}")
            } else {
                log(ERROR, simpleClassName, message)
            }
        }
    }
    
    /**
     * Internal logging method that handles the actual logging
     */
    private fun log(level: Int, tag: String, message: String) {
        // Limit tag length to 23 characters (Android's limit)
        val limitedTag = if (tag.length > 23) tag.take(23) else tag
        
        when (level) {
            VERBOSE -> Log.v(limitedTag, message)
            DEBUG -> Log.d(limitedTag, message)
            INFO -> Log.i(limitedTag, message)
            WARN -> Log.w(limitedTag, message)
            ERROR -> Log.e(limitedTag, message)
        }
    }
}
