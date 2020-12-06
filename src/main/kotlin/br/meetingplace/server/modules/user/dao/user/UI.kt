package br.meetingplace.server.modules.user.dao.user

import br.meetingplace.server.modules.user.dto.requests.RequestUserCreation
import br.meetingplace.server.modules.user.dto.response.UserAuthDTO
import br.meetingplace.server.modules.user.dto.response.UserDTO
import io.ktor.http.*

interface UI {
    fun create(data: RequestUserCreation): HttpStatusCode
    fun readAllByAttribute(name: String?, birthDate: String?, phoneNumber: String?, nationality: String?, city: String?): List<UserDTO>
    fun check(userID: String): Boolean
    fun read(userID: String): UserDTO?
    fun readAll(): List<UserDTO>
    fun readAuthUser(userID: String): UserAuthDTO?
    fun update(userID: String, name: String?, imageURL: String?, about: String?, nationality: String?, phoneNumber: String?, city: String?): HttpStatusCode
    fun delete(userID: String): HttpStatusCode
}