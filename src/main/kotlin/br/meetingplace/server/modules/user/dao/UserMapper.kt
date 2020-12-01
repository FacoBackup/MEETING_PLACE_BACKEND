package br.meetingplace.server.modules.user.dao

import br.meetingplace.server.modules.user.entitie.Social
import br.meetingplace.server.modules.user.entitie.User
import org.jetbrains.exposed.sql.ResultRow

object UserMapper: UserMapperInterface {
    override fun mapUser(it: ResultRow): br.meetingplace.server.modules.user.dto.UserDTO {
        return br.meetingplace.server.modules.user.dto.UserDTO(id = it[User.id], name = it[User.userName],
            email = it[User.email], gender = it[User.gender],
            birthDate =  it[User.birth].toString(), imageURL = it[User.imageURL],
            about = it[User.about], cityOfBirth = it[User.cityOfBirth],
            phoneNumber = it[User.phoneNumber], nationality = it[User.nationality])
    }

    override fun mapSocial(it: ResultRow): br.meetingplace.server.modules.user.dto.SocialDTO {
        return br.meetingplace.server.modules.user.dto.SocialDTO(followedID =  it[Social.followedID], followerID = it[Social.followerID])
    }
}