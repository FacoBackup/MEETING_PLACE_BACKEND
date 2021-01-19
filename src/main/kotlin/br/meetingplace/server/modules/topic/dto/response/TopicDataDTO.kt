package br.meetingplace.server.modules.topic.dto.response

data class TopicDataDTO(
    val id: String,
    val header: String,
    val body: String,
    val approved: Boolean,
    val creatorID: String,
    val creatorName: String,
    val creatorImageURL: String?,
    val mainTopicID: String?,
    val creationDate: Long,
    val imageURL: String?,
    val communityID: String?,
    val communityName: String?,
    val communityImageURL: String?,
    val likes: Long,
    val dislikes: Long,
    val comments: Long,
    val archived: Boolean
)
