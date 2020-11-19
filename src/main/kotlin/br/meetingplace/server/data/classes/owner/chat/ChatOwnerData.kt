package br.meetingplace.server.data.classes.owner.chat

import br.meetingplace.server.data.classes.owner.OwnerType

data class ChatOwnerData(val mainOwnerID: String, val subOwnerID: String, val mainOwnerType: OwnerType, val subOwnerType: OwnerType)