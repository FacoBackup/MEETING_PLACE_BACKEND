package br.meetingplace.server.modules.topic.dto.response

data class TopicDTO (val id: Long,
                     val header: String,
                     val body: String?,
                     val approved: Boolean,
                     val creatorID: Long,
                     val mainTopicID: Long?,
                     val creationDate: Long,
                     val communityID: Long?,
                     val imageURL: String?)