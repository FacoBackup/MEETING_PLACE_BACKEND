package br.meetingplace.server.modules.chat.dto

import br.meetingplace.server.modules.owner.dto.OwnerType

data class ChatOwnerData(val mainOwnerID: String, val subOwnerID: String, val mainOwnerType: OwnerType, val subOwnerType: OwnerType)