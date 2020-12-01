package br.meetingplace.server.modules.community.dao.member

import br.meetingplace.server.modules.community.dto.CommunityMemberDTO
import br.meetingplace.server.modules.community.entitie.CommunityMember
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object CommunityMemberDAO: CMI {
    override fun create(data: CommunityMemberDTO): Status {
        return try{
            transaction {
                CommunityMember.insert {
                    it[communityID] = data.communityID
                    it[userID] = data.userID
                    it[role] = data.role
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun delete(communityID: String, userID: String): Status {

        return try{
            if(transaction { !CommunityMember.select { (CommunityMember.communityID eq communityID) and (CommunityMember.userID eq userID) }.empty() }){

                transaction {
                    CommunityMember.deleteWhere { (CommunityMember.communityID eq communityID) and (CommunityMember.userID eq userID) }
                }

                Status(200, StatusMessages.OK)
            }else Status(404, StatusMessages.NOT_FOUND)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun read(communityID: String, userID: String): CommunityMemberDTO? {
        return try{
            return transaction {
                CommunityMember.select { (CommunityMember.communityID eq communityID) and (CommunityMember.userID eq userID) }.map { mapCommunityMemberDTO(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }

    override fun update(communityID: String, userID: String, role: String): Status {
        return try{
            if(transaction { !CommunityMember.select {  (CommunityMember.communityID eq communityID) and (CommunityMember.userID eq userID) }.empty() }){

                transaction {
                    CommunityMember.update( {  (CommunityMember.communityID eq communityID) and (CommunityMember.userID eq userID) } ){
                        it[this.role] = role
                    }
                }

                Status(200, StatusMessages.OK)
            }else Status(404, StatusMessages.NOT_FOUND)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
    private fun mapCommunityMemberDTO(it: ResultRow): CommunityMemberDTO {
        return CommunityMemberDTO(communityID = it[CommunityMember.communityID], role = it[CommunityMember.role],
            userID = it[CommunityMember.userID])
    }
}