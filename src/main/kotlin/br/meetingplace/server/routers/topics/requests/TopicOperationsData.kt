package br.meetingplace.server.routers.topics.requests

import br.meetingplace.server.routers.generic.requests.Login

data class TopicOperationsData(val identifier: TopicIdentifier, val communityID: String?, val login: Login)