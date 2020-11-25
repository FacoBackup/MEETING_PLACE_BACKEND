package br.meetingplace.server.requests.topics.data

import br.meetingplace.server.requests.generic.data.Login

data class TopicCreationData(
        val header: String,
        val body: String,
        val imageURL: String?,
        val identifier: TopicIdentifier?,
        val communityID: String?,
        val userID:String,
        val mainTopicID: String?
)