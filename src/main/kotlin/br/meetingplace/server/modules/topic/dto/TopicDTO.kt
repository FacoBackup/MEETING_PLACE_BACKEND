package br.meetingplace.server.modules.topic.dto

data class TopicDTO (val id: String, val header: String, val body: String, val approved: Boolean,
                     val footer: String, val creatorID: String, val mainTopicID: String?,
                     val creationDate: String, val communityID: String?, val imageURL: String?)