package br.meetingplace.server.modules.user.dao.social

import br.meetingplace.server.modules.user.dto.SocialDTO
import br.meetingplace.server.modules.user.entitie.Social
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException
import java.util.*

object SocialDAO:SI {

    override fun create(userID: String, followedID: String): Status {
        return try {
            transaction {

            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun delete(userID: String, followedID: String): Status {
        return try {
            transaction {
                Social.deleteWhere {
                    (Social.followedID eq followedID) and
                    (Social.followerID eq userID)
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun read(followedID: String, userID: String): SocialDTO? {
        return try {
            transaction {
                Social.select {
                    (Social.followedID eq followedID) and
                    (Social.followerID eq userID)
                }
            }
            null
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
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