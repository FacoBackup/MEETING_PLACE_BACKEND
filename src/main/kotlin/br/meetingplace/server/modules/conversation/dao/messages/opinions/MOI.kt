package br.meetingplace.server.modules.conversation.dao.messages.opinions

import br.meetingplace.server.modules.conversation.dto.response.messages.MessageOpinionsDTO
import io.ktor.http.*

interface MOI {
    fun create(messageID: Long, userID: Long, liked: Boolean): HttpStatusCode
    fun read(messageID: Long, userID: Long): MessageOpinionsDTO?
    fun update(messageID: Long, userID: Long, liked: Boolean): HttpStatusCode
    fun delete(messageID: Long, userID: Long): HttpStatusCode

}