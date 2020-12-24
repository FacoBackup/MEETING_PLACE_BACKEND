package br.meetingplace.server.modules.user.dao.user

import br.meetingplace.server.modules.user.dto.requests.RequestUserCreation
import br.meetingplace.server.modules.user.dto.response.UserAuthDTO
import br.meetingplace.server.modules.user.dto.response.UserDTO
import io.ktor.http.*

interface UI {
    suspend fun create(data: RequestUserCreation): HttpStatusCode
    suspend fun readAllByAttribute(name: String?, birthDate: String?, phoneNumber: String?, nationality: String?, city: String?): List<UserDTO>
    suspend fun check(userID: String): Boolean
    suspend fun readByID(userID: String): UserDTO?
    suspend fun readByName(name: String): List<UserDTO>
    suspend fun readAll(): List<UserDTO>
    suspend fun readAuthUser(userID: String): UserAuthDTO?
    suspend fun update(userID: String, name: String?, imageURL: String?, about: String?, nationality: String?, phoneNumber: String?, city: String?): HttpStatusCode
    suspend fun delete(userID: String): HttpStatusCode
}