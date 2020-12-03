package br.meetingplace.server.modules.messageTODO.dao.opinions

import br.meetingplace.server.modules.messageTODO.dto.response.MessageOpinionsDTO
import br.meetingplace.server.response.status.Status

interface MOI {
    fun create(messageID: String, userID: String, liked: Boolean): Status
    fun read(messageID: String, userID: String): MessageOpinionsDTO?
    fun update(messageID: String, userID: String, liked: Boolean): Status
    fun delete(messageID: String, userID: String): Status

}