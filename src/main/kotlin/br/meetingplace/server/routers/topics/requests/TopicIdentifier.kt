package br.meetingplace.server.routers.topics.requests

data class TopicIdentifier(val mainTopicID: String, val subTopicID: String?, val mainTopicOwner: String, val community: Boolean)
