package br.meetingplace.server.modules.topic.classes

import org.jetbrains.exposed.sql.Table

data class TopicComments(val mainTopicID: String, val commentID: String): Table()
