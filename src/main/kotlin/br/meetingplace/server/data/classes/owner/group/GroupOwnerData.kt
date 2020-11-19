package br.meetingplace.server.data.classes.owner.group

import br.meetingplace.server.data.classes.owner.OwnerType

data class GroupOwnerData(val groupOwnerID: String, val groupCreatorID: String, val type: OwnerType)