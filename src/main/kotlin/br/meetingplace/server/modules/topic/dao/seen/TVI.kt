package br.meetingplace.server.modules.topic.dao.seen

import io.ktor.http.*

interface TVI {
    fun create(userID: String, topicID: String): HttpStatusCode
    fun check(topicID: String, userID: String): Boolean
}