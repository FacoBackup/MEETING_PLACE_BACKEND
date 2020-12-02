package br.meetingplace.server.modules.user.dao.social

import br.meetingplace.server.modules.user.dto.SocialDTO
import br.meetingplace.server.modules.user.dto.UserDTO
import br.meetingplace.server.request.dto.users.UserCreationDTO
import br.meetingplace.server.response.status.Status

interface SI {
    fun create(userID: String, followedID: String): Status
    fun readAll(userID: String, following:Boolean):List<SocialDTO>
    fun read(followedID: String, userID: String): SocialDTO?
    fun delete(userID: String,followedID: String): Status
}