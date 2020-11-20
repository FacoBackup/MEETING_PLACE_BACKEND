package br.meetingplace.server.requests.topics.data

data class TopicIdentifier(val mainTopicID: String, val subTopicID: String?, val mainTopicOwner: String, val community: Boolean)
