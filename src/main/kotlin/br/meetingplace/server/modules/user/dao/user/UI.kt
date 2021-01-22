package br.meetingplace.server.modules.user.dao.user

import br.meetingplace.server.modules.user.dto.requests.RequestUserCreation
import br.meetingplace.server.modules.user.dto.response.UserAuthDTO
import br.meetingplace.server.modules.user.dto.response.UserDTO
import br.meetingplace.server.modules.user.dto.response.UserSimplifiedDTO
import io.ktor.http.*

interface UI {
    suspend fun create(data: RequestUserCreation): HttpStatusCode
    suspend fun readAllByAttribute(email: String? ,userName: String?,name: String?, birthDate: Long?, phoneNumber: String?, nationality: String?, city: String?): List<UserDTO>
    suspend fun check(userID: Long): Boolean
    suspend fun readByID(userID: Long): UserDTO?
    suspend fun searchUserByMaxID(name: String, requester: Long, maxID: Long): List<UserSimplifiedDTO>
    suspend fun searchUser(name: String, requester: Long): List<UserSimplifiedDTO>
    suspend fun readAll(): List<UserDTO>
    suspend fun readSimplifiedUserByID(userID: Long): UserSimplifiedDTO?
    suspend fun readAuthUser(input: String): UserAuthDTO?
    suspend fun update(userID: Long, name: String?, imageURL: String?, about: String?, nationality: String?, phoneNumber: String?, city: String?, backgroundImageURL: String?, category: String?): HttpStatusCode
    suspend fun delete(userID: Long): HttpStatusCode
}