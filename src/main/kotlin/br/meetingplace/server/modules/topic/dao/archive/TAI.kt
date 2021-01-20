package br.meetingplace.server.modules.topic.dao.archive

import io.ktor.http.*

interface TAI {
    suspend fun create(topicID: String, requester: String): HttpStatusCode
    suspend fun check(topicID: String, requester: String): Boolean
    suspend fun delete(topicID: String, requester: String): HttpStatusCode
}