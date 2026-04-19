package com.example.smishingdetectionapp.util

object DateUtils {

    fun formatRelativeTime(timestamp: Long): String {
        val now = currentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < 60_000 -> "Just now"
            diff < 3_600_000 -> "${diff / 60_000}m ago"
            diff < 86_400_000 -> "${diff /3_600_000}h ago"
            diff < 604_800_000 -> "${diff / 86_400_000}d ago"
            else -> formatTimeStamp(timestamp)
        }
    }

    fun formatTimeStamp(timestamp: Long): String {
        //returns dd/MM/yyyy HH:mm
        val seconds = timestamp / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        // approximate date
        val year = 1970 + (days / 365).toInt()
        val dayOfYear = (days % 365).toInt()
        val month = (dayOfYear / 31) + 1
        val day = (dayOfYear % 31) + 1
        val hour = (hours % 24).toInt()
        val minute = (minutes % 60).toInt()

        return "${day.toString().padStart(2, '0')}/" +
                "${month.toString().padStart(2, '0')}/" +
                "$year " +
                "${hour.toString().padStart(2, '0')}:" +
                minute.toString().padStart(2, '0')

    }
}