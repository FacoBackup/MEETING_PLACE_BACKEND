package br.meetingplace.server.requests.topics

data class RequestTopicCreation(
        val header: String,
        val body: String,
        val imageURL: String?,
        val communityID: String?,
        val userID:String,
        val mainTopicID: String?
)