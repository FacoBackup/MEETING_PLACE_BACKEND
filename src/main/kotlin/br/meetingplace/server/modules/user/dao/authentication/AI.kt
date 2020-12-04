package br.meetingplace.server.modules.user.dao.authentication

import br.meetingplace.server.modules.user.dto.response.AccessLogDTO

interface AI {
    fun read(userID: String): AccessLogDTO?
    fun create(userID: String, ip: String): Status
    fun delete(userID: String): Status
}