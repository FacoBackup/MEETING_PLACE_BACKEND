package br.meetingplace.server.requests.topics.operators

import br.meetingplace.server.requests.generic.data.Login
import br.meetingplace.server.requests.topics.data.TopicIdentifier

data class TopicSimpleOperator(val topicID: String, val userID: String)