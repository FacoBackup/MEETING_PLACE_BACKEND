package br.meetingplace.server.modules.user.dao

import br.meetingplace.server.modules.user.dto.UserDTO
import br.meetingplace.server.request.dto.users.UserCreationDTO
import br.meetingplace.server.response.status.Status

interface UI {
    fun create(data: UserCreationDTO): Status
    fun readAll(name: String?, birthDate: String?, phoneNumber: String?, nationality: String?, city: String?): List<UserDTO>
    fun read(userID: String): UserDTO?
    fun update(userID: String, name: String?, about: String?, nationality: String?, phoneNumber: String?, city: String?): Status
    fun delete(userID: String): Status
}