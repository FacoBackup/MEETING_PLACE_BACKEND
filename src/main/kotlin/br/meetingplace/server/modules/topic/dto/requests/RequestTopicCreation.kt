package br.meetingplace.server.modules.topic.dto.requests

data class RequestTopicCreation(
        val header: String,
        val body: String?,
        val imageURL: String?,
        val communityID: Long?,
        val mainTopicID: Long?,
        val hashTags: List<String>,
        val mentionedUsers: List<String>
)