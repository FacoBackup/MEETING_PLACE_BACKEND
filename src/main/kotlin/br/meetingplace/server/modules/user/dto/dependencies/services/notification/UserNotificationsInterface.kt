package br.meetingplace.server.modules.user.dto.dependencies.services.notification

import br.meetingplace.server.modules.global.dto.notification.NotificationData

interface UserNotificationsInterface {
    fun updateInbox(notification: NotificationData)
    fun clearNotifications()
}