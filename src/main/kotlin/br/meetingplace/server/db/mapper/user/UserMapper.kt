package br.meetingplace.server.db.mapper.user

import br.meetingplace.server.modules.user.dto.UserDTO
import br.meetingplace.server.modules.user.dto.UserFollowersDTO
import org.jetbrains.exposed.sql.ResultRow

class UserMapper: UserMapperInterface {
    override fun mapUser(it: ResultRow): UserDTO {
        TODO("Not yet implemented")
    }

    override fun mapUserFollowers(it: ResultRow): UserFollowersDTO {
        TODO("Not yet implemented")
    }
}