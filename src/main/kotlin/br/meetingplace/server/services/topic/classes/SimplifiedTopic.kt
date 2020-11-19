package br.meetingplace.server.services.topic.classes

import br.meetingplace.server.data.classes.owner.topic.TopicOwnerData

data class SimplifiedTopic(val ID: String, val owner: TopicOwnerData)
