package br.meetingplace.server.modules.user.dao

import br.meetingplace.server.modules.user.dto.UserDTO
import br.meetingplace.server.modules.user.dto.SocialDTO
import org.jetbrains.exposed.sql.ResultRow

interface UserMapperInterface {
    fun mapUser(it: ResultRow):UserDTO
    fun mapSocial(it: ResultRow):SocialDTO
}