package br.meetingplace.server.db.mapper.user

import br.meetingplace.server.modules.user.db.Social
import br.meetingplace.server.modules.user.db.User
import br.meetingplace.server.modules.user.dto.UserDTO
import org.jetbrains.exposed.sql.ResultRow

class UserMapper: UserMapperInterface {
    override fun mapUser(it: ResultRow): UserDTO {
        return UserDTO(id = it[User.id], name = it[User.userName], email = it[User.email], gender = it[User.gender], birthDate = it[User.birth].toString("dd-MM-yyyy"), imageURL = it[User.imageURL], about = it[User.about], cityOfBirth = it[User.cityOfBirth], phoneNumber = it[User.phoneNumber])
    }

    override fun mapSocial(it: ResultRow): br.meetingplace.server.modules.user.dto.SocialDTO {
        return br.meetingplace.server.modules.user.dto.SocialDTO(followedID =  it[Social.followedID], followerID = it[Social.followerID])
    }
}