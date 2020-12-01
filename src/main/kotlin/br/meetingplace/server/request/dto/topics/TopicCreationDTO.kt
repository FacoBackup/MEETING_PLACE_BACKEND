package br.meetingplace.server.request.dto.topics

data class TopicCreationDTO(
        val header: String,
        val body: String,
        val imageURL: String?,
        val communityID: String?,
        val userID:String,
        val mainTopicID: String?
)