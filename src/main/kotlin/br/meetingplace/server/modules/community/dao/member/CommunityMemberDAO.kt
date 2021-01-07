package br.meetingplace.server.modules.community.dao.member

import br.meetingplace.server.modules.community.dto.response.CommunityMemberDTO
import br.meetingplace.server.modules.community.entities.CommunityMemberEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object CommunityMemberDAO: CMI {
    override suspend fun readByUser(userID: String): List<CommunityMemberDTO> {
        return try{
            transaction {
                CommunityMemberEntity.select {
                    CommunityMemberEntity.userID eq userID
                }.map { mapCommunityMemberDTO(it) }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    override suspend fun create(userID: String, communityID: String, role: String): HttpStatusCode {
        return try{
            transaction {
                CommunityMemberEntity.insert {
                    it[this.communityID] = communityID
                    it[this.userID] = userID
                    it[this.role] = role
                }
            }
            HttpStatusCode.Created
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun delete(communityID: String, userID: String): HttpStatusCode {
        return try{
            transaction {
                CommunityMemberEntity.deleteWhere { (CommunityMemberEntity.communityID eq communityID) and (CommunityMemberEntity.userID eq userID) }
            }

            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun check(communityID: String, userID: String): HttpStatusCode {
        return try{
            if(transaction {
                CommunityMemberEntity.select { (CommunityMemberEntity.communityID eq communityID) and (CommunityMemberEntity.userID eq userID) }.empty()
            }) HttpStatusCode.NotFound
            else HttpStatusCode.Found
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    override suspend fun read(communityID: String, userID: String): CommunityMemberDTO? {
        return try{
            return transaction {
                CommunityMemberEntity.select { (CommunityMemberEntity.communityID eq communityID) and
                        (CommunityMemberEntity.userID eq userID) }
                    .map { mapCommunityMemberDTO(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }

    override suspend fun update(communityID: String, userID: String, role: String): HttpStatusCode {
        return try{
            transaction {
                CommunityMemberEntity.update( {  (CommunityMemberEntity.communityID eq communityID) and (CommunityMemberEntity.userID eq userID) } ){
                    it[this.role] = role
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    private fun mapCommunityMemberDTO(it: ResultRow): CommunityMemberDTO {
        return CommunityMemberDTO(communityID = it[CommunityMemberEntity.communityID], role = it[CommunityMemberEntity.role],
            userID = it[CommunityMemberEntity.userID])
    }
}