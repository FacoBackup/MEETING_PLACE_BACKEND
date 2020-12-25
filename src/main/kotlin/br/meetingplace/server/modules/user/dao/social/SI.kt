package br.meetingplace.server.modules.user.dao.social

import br.meetingplace.server.modules.user.dto.response.SocialDTO
import br.meetingplace.server.modules.user.dto.response.UserSocialDTO
import io.ktor.http.*

interface SI {
    suspend fun create(userID: String, followedID: String): HttpStatusCode
    suspend fun readAll(userID: String, following:Boolean):List<SocialDTO>
    suspend fun check(followedID: String, userID: String): Boolean
    suspend fun delete(userID: String,followedID: String): HttpStatusCode

}