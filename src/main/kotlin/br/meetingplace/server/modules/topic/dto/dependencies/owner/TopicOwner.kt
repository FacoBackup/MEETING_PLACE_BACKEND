package br.meetingplace.server.modules.topic.dto.dependencies.owner

import br.meetingplace.server.modules.owner.dto.OwnerType

data class TopicOwner(val mainTopicOwnerID: String, val mainTopicCreatorID: String, val type: OwnerType)