package br.meetingplace.server.modules.user.dao.social

import br.meetingplace.server.modules.user.dto.response.SocialDTO
import br.meetingplace.server.modules.user.entities.Social
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object UserSocialDAO:SI {

    override fun create(userID: String, followedID: String): HttpStatusCode {
        return try {
            transaction {
                Social.insert{
                    it[Social.followedID] = followedID
                    it[Social.followerID] = userID
                }
            }
            HttpStatusCode.Created
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override fun delete(userID: String, followedID: String): HttpStatusCode {
        return try {
            transaction {
                Social.deleteWhere {
                    (Social.followedID eq followedID) and
                    (Social.followerID eq userID)
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override fun check(followedID: String, userID: String): Boolean {
        return try {
            return !transaction {
                Social.select {
                    (Social.followedID eq followedID) and
                            (Social.followerID eq userID)
                }.empty()}
        }catch (normal: Exception){
           false
        }catch (psql: PSQLException){
            false
        }
    }

    override fun readAll(userID: String, following: Boolean): List<SocialDTO> {
        return try {
            when(following){
                true-> transaction {
                        Social.select {
                            Social.followerID eq userID
                        }.map { mapSocial(it) }
                    }
                false-> transaction {
                    Social.select {
                        Social.followedID eq userID
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
        return SocialDTO(followedID =  it[Social.followedID], followerID = it[Social.followerID])
    }
}