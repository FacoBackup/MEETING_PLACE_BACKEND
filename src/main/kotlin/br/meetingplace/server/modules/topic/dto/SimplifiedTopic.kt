package br.meetingplace.server.modules.topic.dto

import br.meetingplace.server.modules.topic.dto.dependencies.owner.TopicOwner

data class SimplifiedTopic(val ID: String, val owner: TopicOwner)
