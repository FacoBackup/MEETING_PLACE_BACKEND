package br.meetingplace.server.routers.topics.requests

import br.meetingplace.server.routers.generic.requests.Login

data class TopicData(
        val header: String,
        val body: String,
        val identifier: TopicIdentifier?,
        val communityID: String?,
        val login: Login
)