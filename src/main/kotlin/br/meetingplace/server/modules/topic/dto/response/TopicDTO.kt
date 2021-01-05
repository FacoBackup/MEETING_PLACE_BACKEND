package br.meetingplace.server.modules.topic.dto.response

data class TopicDTO (val id: String,
                     val header: String,
                     val body: String,
                     val approved: Boolean,
                     val creatorID: String,
                     val mainTopicID: String?,
                     val creationDate: Long,
                     val communityID: String?,
                     val imageURL: String?)