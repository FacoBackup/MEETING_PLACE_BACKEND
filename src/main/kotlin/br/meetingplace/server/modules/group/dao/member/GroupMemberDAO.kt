package br.meetingplace.server.modules.group.dao.member

import br.meetingplace.server.modules.group.dto.response.GroupMemberDTO
import br.meetingplace.server.modules.group.entities.GroupMember
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object GroupMemberDAO: GMI {

    override fun create(userID: String, groupID: String, admin: Boolean): Status {
        return try {
            transaction {
                GroupMember.insert {
                    it[this.groupID] = groupID
                    it[this.userID] = userID
                    it[this.admin] = admin
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun delete(userID: String, groupID: String): Status {
        return try {
            transaction {
                GroupMember.deleteWhere { (GroupMember.groupID eq groupID) and (GroupMember.userID eq userID) }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
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

    override fun update(userID: String, groupID: String, admin: Boolean): Status {
        return try {
            transaction {
                GroupMember.update({ (GroupMember.groupID eq groupID) and (GroupMember.userID eq userID) }) {
                    it[this.admin] = admin
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
    private fun mapGroupMembers(it: ResultRow): GroupMemberDTO {
        return GroupMemberDTO(groupID = it[GroupMember.groupID], admin = it[GroupMember.admin], userID = it[GroupMember.userID])
    }
}