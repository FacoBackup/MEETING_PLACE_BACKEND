package br.meetingplace.server.data.classes.owner.topic

import br.meetingplace.server.data.classes.owner.OwnerType

data class TopicOwnerData(val mainTopicOwnerID: String, val mainTopicCreatorID: String, val type: OwnerType)