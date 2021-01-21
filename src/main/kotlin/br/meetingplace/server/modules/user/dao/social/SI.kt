package br.meetingplace.server.modules.user.dao.social

import br.meetingplace.server.modules.user.dto.response.SocialDTO
import io.ktor.http.*

interface SI {
    suspend fun create(userID: Long, followedID: Long): HttpStatusCode
    suspend fun readAll(userID: Long, following:Boolean):List<SocialDTO>
    suspend fun check(followedID: Long, userID: Long): Boolean
    suspend fun delete(userID: Long,followedID: Long): HttpStatusCode

}