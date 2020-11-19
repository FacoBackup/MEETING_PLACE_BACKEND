package br.meetingplace.server.data.classes.notification

import br.meetingplace.server.data.classes.notification.data.NotificationMainType
import br.meetingplace.server.data.classes.notification.data.NotificationSubType

data class NotificationData(val mainType: NotificationMainType, val subType: NotificationSubType)