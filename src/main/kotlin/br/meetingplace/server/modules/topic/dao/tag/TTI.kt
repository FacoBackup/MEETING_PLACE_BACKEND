package br.meetingplace.server.modules.topic.dao.tag

import io.ktor.http.*

interface TTI {
    suspend fun create(topicID: Long, tagID: Long): HttpStatusCode
    suspend fun delete(topicID: Long): HttpStatusCode
}