package br.meetingplace.server.modules.user.dao.user

import br.meetingplace.server.modules.user.dto.requests.RequestUserCreation
import br.meetingplace.server.modules.user.dto.response.UserAuthDTO
import br.meetingplace.server.modules.user.dto.response.UserDTO
import br.meetingplace.server.modules.user.dto.response.UserSocialDTO
import io.ktor.http.*

interface UI {
    suspend fun create(data: RequestUserCreation): HttpStatusCode
    suspend fun readAllByAttribute(name: String?, birthDate: Long?, phoneNumber: String?, nationality: String?, city: String?): List<UserDTO>
    suspend fun check(userID: String): Boolean
    suspend fun readByID(userID: String): UserDTO?
    suspend fun readByName(name: String, requester: String): List<UserDTO>
    suspend fun readAll(): List<UserDTO>
    suspend fun readSocialByID(userID: String): UserSocialDTO?
    suspend fun readAuthUser(userID: String): UserAuthDTO?
    suspend fun update(userID: String, name: String?, imageURL: String?, about: String?, nationality: String?, phoneNumber: String?, city: String?): HttpStatusCode
    suspend fun delete(userID: String): HttpStatusCode
}