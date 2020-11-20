package br.meetingplace.server.modules.topic.dto

import br.meetingplace.server.modules.owner.dto.OwnerType

data class TopicOwnerData(val mainTopicOwnerID: String, val mainTopicCreatorID: String, val type: OwnerType)