package br.meetingplace.server.modules.group.dao

import br.meetingplace.server.modules.group.entitie.Group
import br.meetingplace.server.modules.group.entitie.GroupMember
import br.meetingplace.server.modules.group.dto.GroupDTO
import br.meetingplace.server.modules.group.dto.GroupMemberDTO
import br.meetingplace.server.request.dto.group.GroupCreationDTO
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.postgresql.util.PSQLException
import java.util.*

object GroupDAO: GI {

    override fun create(data: GroupCreationDTO, approved: Boolean): Status {
        return try {
            transaction {
                Group.insert {
                    it[id] = UUID.randomUUID().toString()
                    it[creationDate] = DateTime.now()
                    it[communityID] = data.communityID
                    it[imageURL] = data.imageURL
                    it[this.approved] = approved
                    it[about] = data.about
                    it[name] = data.name
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun delete(groupID: String): Status {
        return try {
            transaction {
                Group.deleteWhere { Group.id eq groupID }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun read(groupID: String): GroupDTO? {
        return try {
            transaction {
                Group.select { Group.id eq groupID }.map { mapGroup(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }

    override fun update(groupID: String, name: String?, about: String?, approved: Boolean): Status {
        return try {
            transaction {
                Group.update({Group.id eq groupID}) {
                    if(!name.isNullOrBlank())
                        it[this.name] = name
                    if(!about.isNullOrBlank())
                        it[this.about] = about
                    it[this.approved] = approved
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
    private fun mapGroup(it: ResultRow): GroupDTO {
        return GroupDTO(communityID = it[Group.communityID], id = it[Group.id], name = it[Group.name], imageURL = it[Group.imageURL], creationDate = it[Group.creationDate].toString("dd-MM-yyyy"), about = it[Group.about], approved = it[Group.approved])
    }

}