package br.meetingplace.server.user.classes.dependencies.services.notification

import br.meetingplace.server.data.classes.notification.NotificationData

interface UserNotificationsInterface {
    fun updateInbox(notification: NotificationData)
    fun clearNotifications()
}