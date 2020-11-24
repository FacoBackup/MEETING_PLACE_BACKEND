package br.meetingplace.server.modules.topic.classes

import org.jetbrains.exposed.sql.Table

data class TopicOpinions (var liked: Boolean, val userID: String, val topicID: String): Table()