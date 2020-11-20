package br.meetingplace.server.requests.topics

import br.meetingplace.server.requests.generic.Login

data class TopicData(
        val header: String,
        val body: String,
        val identifier: TopicIdentifier?,
        val communityID: String?,
        val login: Login
)