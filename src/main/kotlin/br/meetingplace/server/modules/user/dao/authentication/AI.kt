package br.meetingplace.server.modules.user.dao.authentication

import br.meetingplace.server.modules.user.dto.response.AuthenticationDTO
import br.meetingplace.server.response.status.Status

interface AI {
    fun read(userID: String): AuthenticationDTO?
    fun create(userID: String): Status
    fun delete(userID: String): Status
}