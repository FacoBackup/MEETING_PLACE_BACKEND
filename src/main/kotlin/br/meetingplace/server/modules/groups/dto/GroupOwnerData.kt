package br.meetingplace.server.modules.groups.dto

import br.meetingplace.server.modules.owner.dto.OwnerType

data class GroupOwnerData(val groupOwnerID: String, val groupCreatorID: String, val type: OwnerType)