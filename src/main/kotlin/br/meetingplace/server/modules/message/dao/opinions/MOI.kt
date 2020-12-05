package br.meetingplace.server.modules.message.dao.opinions

import br.meetingplace.server.modules.message.dto.response.MessageOpinionsDTO
import io.ktor.http.*

interface MOI {
    fun create(messageID: String, userID: String, liked: Boolean): HttpStatusCode
    fun read(messageID: String, userID: String): MessageOpinionsDTO?
    fun update(messageID: String, userID: String, liked: Boolean): HttpStatusCode
    fun delete(messageID: String, userID: String): HttpStatusCode

}