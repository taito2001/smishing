package com.example.smishingdetectionapp.notifications

data class NotificationType (
    val id: String,            // unique id for this type
    val channelId: String,     // Android channelId / iOS equivalent
    val channelName: String,   // human-friendly channel name
    val channelDesc: String,   // channel description
    val key: String,             // preferences key for isEnabled
    val priority: NotificationPriority = NotificationPriority.DEFAULT
)

enum class NotificationPriority {
    HIGH, DEFAULT, LOW
}

object NotificationTypes {
    val smishingDetectionAlert = NotificationType(
        id = "smish_detection_alert",
        channelName = "detection_channel_name",
        channelId = "detection_channel_id",
        channelDesc = "detection_channel_desc",
        key = "smish_notif_pref",
        priority = NotificationPriority.HIGH
    )
    val SpamDetectionAlert = NotificationType(
        id = "spam_detection_alert",
        channelName = "detection_channel_name",
        channelId = "detection_channel_id",
        channelDesc = "detection_channel_desc",
        key = "spam_notif_pref",
        priority = NotificationPriority.HIGH
    )
    val newsAlert = NotificationType(
        id = "news_alert",
        channelName = "push_channel_name",
        channelId = "push_channel_id",
        channelDesc = "push_channel_desc",
        key = "news_notif_pref",
        priority = NotificationPriority.DEFAULT
    )
    val incidentAlert = NotificationType(
        id = "incident_alert",
        channelName = "push_channel_name",
        channelId = "push_channel_id",
        channelDesc = "push_channel_desc",
        key = "incident_notif_pref",
        priority = NotificationPriority.DEFAULT
    )
    val updateNotification = NotificationType(
        id = "update_notification",
        channelName = "system_channel_name",
        channelId = "system_channel_id",
        channelDesc = "system_channel_desc",
        key = "update_notif_pref",
        priority = NotificationPriority.DEFAULT
    )
    val backupNotification = NotificationType(
        id = "backup_notification",
        channelName = "system_channel_name",
        channelId = "system_channel_id",
        channelDesc = "system_channel_desc",
        key = "backup_notif_pref",
        priority = NotificationPriority.DEFAULT
    )

    val passwordNotification = NotificationType(
        id = "password_notification",
        channelName = "system_channel_name",
        channelId = "system_channel_id",
        channelDesc = "system_channel_desc",
        key = "password_notif_pref",
        priority = NotificationPriority.DEFAULT
    )

}