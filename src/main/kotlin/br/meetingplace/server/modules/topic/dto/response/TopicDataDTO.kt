package br.meetingplace.server.modules.topic.dto.response

data class TopicDataDTO(
    val id: String,
    val header: String,
    val body: String,
    val approved: Boolean,
    val creatorID: String,
    val mainTopicID: String?,
    val creationDate: Long,
    val communityID: String?,
    val imageURL: String?,
    val subjectName: String,
    val communityName: String?,
    val subjectImageURL: String?
)
