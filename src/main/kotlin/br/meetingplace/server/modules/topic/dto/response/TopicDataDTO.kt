package br.meetingplace.server.modules.topic.dto.response

data class TopicDataDTO(
    val id: Long,
    val header: String,
    val body: String,
    val approved: Boolean,
    val creatorID: Long,
    val creatorName: String,
    val creatorImageURL: String?,
    val mainTopicID: Long?,
    val creationDate: Long,
    val imageURL: String?,
    val communityID: Long?,
    val communityName: String?,
    val communityImageURL: String?,
    val comments: Long,
    val archived: Boolean,
    val liked: Boolean,
    val disliked: Boolean
)
