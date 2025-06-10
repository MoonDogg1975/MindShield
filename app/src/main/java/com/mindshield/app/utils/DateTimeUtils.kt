package com.mindshield.app.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    const val DATE_FORMAT_SERVER = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val DATE_FORMAT_DISPLAY = "MMM dd, yyyy"
    const val TIME_FORMAT_12H = "h:mm a"
    const val TIME_FORMAT_24H = "HH:mm"
    const val DATE_TIME_FORMAT_DISPLAY = "MMM dd, yyyy hh:mm a"

    /**
     * Get current timestamp in milliseconds
     */
    fun getCurrentTimestamp(): Long {
        return System.currentTimeMillis()
    }

    /**
     * Format timestamp to display string
     */
    fun formatTimestamp(timestamp: Long, format: String): String {
        return try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.timeZone = TimeZone.getDefault()
            sdf.format(Date(timestamp))
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * Parse date string to timestamp
     */
    fun parseDate(dateString: String, format: String): Long {
        return try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val date = sdf.parse(dateString)
            date?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * Get relative time span (e.g., "2 hours ago", "3 days ago")
     */
    fun getRelativeTimeSpanString(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days > 0 -> "$days ${if (days == 1L) "day" else "days"} ago"
            hours > 0 -> "$hours ${if (hours == 1L) "hour" else "hours"} ago"
            minutes > 0 -> "$minutes ${if (minutes == 1L) "minute" else "minutes"} ago"
            else -> "Just now"
        }
    }

    /**
     * Check if two timestamps are on the same day
     */
    fun isSameDay(timestamp1: Long, timestamp2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = timestamp1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = timestamp2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    /**
     * Get start of day timestamp (00:00:00)
     */
    fun getStartOfDay(timestamp: Long): Long {
        val cal = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }

    /**
     * Get end of day timestamp (23:59:59.999)
     */
    fun getEndOfDay(timestamp: Long): Long {
        val cal = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return cal.timeInMillis
    }
}
