package br.meetingplace.server.dto.notification

import br.meetingplace.server.dto.notification.types.NotificationMainType
import br.meetingplace.server.dto.notification.types.NotificationSubType

data class NotificationData(val mainType: NotificationMainType, val subType: NotificationSubType)