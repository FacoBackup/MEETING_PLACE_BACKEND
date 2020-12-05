package br.meetingplace.server.modules.group.dao.member

import br.meetingplace.server.modules.group.dto.response.GroupMemberDTO
import br.meetingplace.server.modules.group.entities.GroupMember
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object GroupMemberDAO: GMI {

    override fun create(userID: String, groupID: String, admin: Boolean): HttpStatusCode {
        return try {
            transaction {
                GroupMember.insert {
                    it[this.groupID] = groupID
                    it[this.userID] = userID
                    it[this.admin] = admin
                }
            }
            HttpStatusCode.Created
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    override fun delete(userID: String, groupID: String): HttpStatusCode {
        return try {
            transaction {
                GroupMember.deleteWhere { (GroupMember.groupID eq groupID) and (GroupMember.userID eq userID) }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override fun check(groupID: String, userID: String): HttpStatusCode {
        return try {
            if(transaction {
                GroupMember.select { (GroupMember.groupID eq groupID) and (GroupMember.userID eq userID) }.empty()
            }) HttpStatusCode.NotFound
            else HttpStatusCode.Found
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    override fun read(userID: String, groupID: String): GroupMemberDTO? {
        return try {
            transaction {
                GroupMember.select { (GroupMember.groupID eq groupID) and (GroupMember.userID eq userID) }.map { mapGroupMembers(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }

    override fun update(userID: String, groupID: String, admin: Boolean): HttpStatusCode {
        return try {
            transaction {
                GroupMember.update({ (GroupMember.groupID eq groupID) and (GroupMember.userID eq userID) }) {
                    it[this.admin] = admin
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    private fun mapGroupMembers(it: ResultRow): GroupMemberDTO {
        return GroupMemberDTO(
            groupID = it[GroupMember.groupID],
            admin = it[GroupMember.admin],
            userID = it[GroupMember.userID])
    }
}