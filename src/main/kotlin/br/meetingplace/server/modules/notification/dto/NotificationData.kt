package br.meetingplace.server.modules.notification.dto

import br.meetingplace.server.modules.notification.dto.types.NotificationMainType
import br.meetingplace.server.modules.notification.dto.types.NotificationSubType

data class NotificationData(val mainType: NotificationMainType, val subType: NotificationSubType, val ID: String)