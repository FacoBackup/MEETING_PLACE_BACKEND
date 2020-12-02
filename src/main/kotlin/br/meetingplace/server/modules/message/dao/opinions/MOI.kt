package br.meetingplace.server.modules.message.dao.opinions

import br.meetingplace.server.modules.group.dto.GroupDTO
import br.meetingplace.server.modules.message.dto.MessageOpinionsDTO
import br.meetingplace.server.request.dto.group.GroupCreationDTO
import br.meetingplace.server.response.status.Status

interface MOI {
    fun create(messageID: String, userID: String, liked: Boolean): Status
    fun read(messageID: String, userID: String): MessageOpinionsDTO?
    fun update(messageID: String, userID: String, liked: Boolean): Status
    fun delete(messageID: String, userID: String): Status

}