package br.meetingplace.server.modules.global.dto.notification

import br.meetingplace.server.modules.global.dto.notification.types.NotificationMainType
import br.meetingplace.server.modules.global.dto.notification.types.NotificationSubType

data class NotificationData(val mainType: NotificationMainType, val subType: NotificationSubType, val ID: String, val ownerID: String)