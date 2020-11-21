package br.meetingplace.server.modules.user.dto.dependencies.services.notification

import br.meetingplace.server.modules.global.dto.notification.NotificationData

class UserNotifications private constructor() : UserNotificationsInterface {
    companion object {
        private val Class = UserNotifications()
        fun getClass() = Class
    }

    private var inbox = mutableListOf<NotificationData>()

    override fun clearNotifications() {
        inbox.clear()
    }

    override fun updateInbox(notification: NotificationData) {
        inbox.add(notification)
    }
}