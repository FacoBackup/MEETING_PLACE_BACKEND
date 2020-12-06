package br.meetingplace.server.modules.group.dao

import br.meetingplace.server.modules.group.dto.requests.RequestGroupCreation
import br.meetingplace.server.modules.group.dto.response.GroupDTO
import br.meetingplace.server.modules.group.entities.Group
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.postgresql.util.PSQLException
import java.util.*

object GroupDAO: GI {

    override fun create(data: RequestGroupCreation, approved: Boolean): HttpStatusCode {
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
            HttpStatusCode.Created
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override fun check(groupID: String): Boolean {
        return try {
            !transaction {
                Group.select { Group.id eq groupID }.empty()
            }
        }catch (normal: Exception){
            false
        }catch (psql: PSQLException){
            false
        }
    }

    override fun delete(groupID: String): HttpStatusCode {
        return try {
            transaction {
                Group.deleteWhere { Group.id eq groupID }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
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

    override fun update(groupID: String, name: String?, about: String?, approved: Boolean): HttpStatusCode {
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
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    private fun mapGroup(it: ResultRow): GroupDTO {
        return GroupDTO(communityID = it[Group.communityID], id = it[Group.id], name = it[Group.name], imageURL = it[Group.imageURL], creationDate = it[Group.creationDate].toString("dd-MM-yyyy"), about = it[Group.about], approved = it[Group.approved])
    }

}