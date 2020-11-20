package br.meetingplace.server.requests.topics

import br.meetingplace.server.requests.generic.Login

data class TopicOperationsData(val identifier: TopicIdentifier, val communityID: String?, val login: Login)