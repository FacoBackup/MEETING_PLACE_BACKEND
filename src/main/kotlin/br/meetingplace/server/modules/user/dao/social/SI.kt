package br.meetingplace.server.modules.user.dao.social

import br.meetingplace.server.modules.user.dto.response.SocialDTO
import io.ktor.http.*

interface SI {
    fun create(userID: String, followedID: String): HttpStatusCode
    fun readAll(userID: String, following:Boolean):List<SocialDTO>
    fun check(followedID: String, userID: String): Boolean
    fun delete(userID: String,followedID: String): HttpStatusCode
}