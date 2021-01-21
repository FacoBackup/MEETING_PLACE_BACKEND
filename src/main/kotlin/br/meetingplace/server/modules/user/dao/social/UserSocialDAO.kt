package br.meetingplace.server.modules.user.dao.social

import br.meetingplace.server.modules.user.dto.response.SocialDTO
import br.meetingplace.server.modules.user.entities.UserSocialEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object UserSocialDAO:SI {

    override suspend fun create(userID: Long, followedID: Long): HttpStatusCode {
        return try {
            transaction {
                UserSocialEntity.insert{
                    it[UserSocialEntity.followedID] = followedID
                    it[UserSocialEntity.followerID] = userID
                }
            }
            HttpStatusCode.Created
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun delete(userID: Long, followedID: Long): HttpStatusCode {
        return try {
            transaction {
                UserSocialEntity.deleteWhere {
                    (UserSocialEntity.followedID eq followedID) and
                    (UserSocialEntity.followerID eq userID)
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun check(followedID: Long, userID: Long): Boolean {
        return try {
            return !transaction {
                UserSocialEntity.select {
                    (UserSocialEntity.followedID eq followedID) and
                            (UserSocialEntity.followerID eq userID)
                }.empty()}
        }catch (normal: Exception){
           false
        }catch (psql: PSQLException){
            false
        }
    }

    override  suspend fun readAll(userID: Long, following: Boolean): List<SocialDTO> {
        return try {
            when(following){
                true-> transaction {
                        UserSocialEntity.select {
                            UserSocialEntity.followerID eq userID
                        }.map { mapSocial(it) }
                    }
                false-> transaction {
                    UserSocialEntity.select {
                        UserSocialEntity.followedID eq userID
                    }.map { mapSocial(it) }
                }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    private fun mapSocial(it: ResultRow): SocialDTO {
        return SocialDTO(followedID =  it[UserSocialEntity.followedID], followerID = it[UserSocialEntity.followerID])
    }
}