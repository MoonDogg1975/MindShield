package com.mindshield.app.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetworkUtils {
    /**
     * Check if the device has internet connection
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo != null && networkInfo.isConnected
        }
    }

    /**
     * Get the network state as a human-readable string
     */
    fun getNetworkState(context: Context): String {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return "Disconnected"
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return "Disconnected"

            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Connected to WiFi"
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Connected to Mobile Data"
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Connected to Ethernet"
                else -> "Connected to Unknown Network"
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected) {
                when (networkInfo.type) {
                    ConnectivityManager.TYPE_WIFI -> "Connected to WiFi"
                    ConnectivityManager.TYPE_MOBILE -> "Connected to Mobile Data"
                    ConnectivityManager.TYPE_ETHERNET -> "Connected to Ethernet"
                    else -> "Connected to Unknown Network"
                }
            } else {
                "Disconnected"
            }
        }
    }
}
