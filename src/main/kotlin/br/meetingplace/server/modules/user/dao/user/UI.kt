package br.meetingplace.server.modules.user.dao.user

import br.meetingplace.server.modules.user.dto.response.UserDTO
import br.meetingplace.server.modules.user.dto.requests.RequestUserCreation
import br.meetingplace.server.response.status.Status

interface UI {
    fun create(data: RequestUserCreation): Status
    fun readAllByAttribute(name: String?, birthDate: String?, phoneNumber: String?, nationality: String?, city: String?): List<UserDTO>
    fun read(userID: String): UserDTO?
    fun readAll(): List<UserDTO>
    fun update(userID: String, name: String?, imageURL: String?, about: String?, nationality: String?, phoneNumber: String?, city: String?): Status
    fun delete(userID: String): Status
}