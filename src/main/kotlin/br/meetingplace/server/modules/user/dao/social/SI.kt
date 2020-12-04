package br.meetingplace.server.modules.user.dao.social

import br.meetingplace.server.modules.user.dto.response.SocialDTO

interface SI {
    fun create(userID: String, followedID: String): Status
    fun readAll(userID: String, following:Boolean):List<SocialDTO>
    fun read(followedID: String, userID: String): SocialDTO?
    fun delete(userID: String,followedID: String): Status
}