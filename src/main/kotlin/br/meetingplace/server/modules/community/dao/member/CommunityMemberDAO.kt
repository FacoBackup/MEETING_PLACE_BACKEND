package br.meetingplace.server.modules.community.dao.member

import br.meetingplace.server.modules.community.dto.response.CommunityMemberDTO
import br.meetingplace.server.modules.community.entities.CommunityMember
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object CommunityMemberDAO: CMI {
    override fun create(userID: String, communityID: String, role: String): HttpStatusCode {
        return try{
            transaction {
                CommunityMember.insert {
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

    override fun delete(communityID: String, userID: String): HttpStatusCode {
        return try{
            transaction {
                CommunityMember.deleteWhere { (CommunityMember.communityID eq communityID) and (CommunityMember.userID eq userID) }
            }

            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override fun check(communityID: String, userID: String): HttpStatusCode {
        return try{
            if(transaction {
                CommunityMember.select { (CommunityMember.communityID eq communityID) and (CommunityMember.userID eq userID) }.empty()
            }) HttpStatusCode.NotFound
            else HttpStatusCode.Found
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    override fun read(communityID: String, userID: String): CommunityMemberDTO? {
        return try{
            return transaction {
                CommunityMember.select { (CommunityMember.communityID eq communityID) and
                        (CommunityMember.userID eq userID) }
                    .map { mapCommunityMemberDTO(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }

    override fun update(communityID: String, userID: String, role: String): HttpStatusCode {
        return try{
            transaction {
                CommunityMember.update( {  (CommunityMember.communityID eq communityID) and (CommunityMember.userID eq userID) } ){
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
        return CommunityMemberDTO(communityID = it[CommunityMember.communityID], role = it[CommunityMember.role],
            userID = it[CommunityMember.userID])
    }
}