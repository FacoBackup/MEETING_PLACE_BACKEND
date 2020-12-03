package br.meetingplace.server.modules.community.dao

import br.meetingplace.server.modules.community.entities.Community
import br.meetingplace.server.modules.community.dto.response.CommunityDTO
import br.meetingplace.server.modules.community.dto.requests.RequestCommunityCreation
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.postgresql.util.PSQLException
import java.util.*

object CommunityDAO: CI {

    override fun create(data: RequestCommunityCreation):Status  {
        return try{
            transaction {
                Community.insert {
                    it[name] = data.name
                    it[id] = UUID.randomUUID().toString()
                    it[imageURL] = data.imageURL
                    it[creationDate] =  DateTime.now()
                    it[about] = data.about
                    it[location] = data.location
                    it[parentCommunityID]= data.parentCommunityID
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun delete(id: String):Status  {
        return try{
            if(transaction { !Community.select { Community.id eq id }.empty() }){

                transaction {
                    Community.deleteWhere { Community.id eq id }
                }

                Status(200, StatusMessages.OK)
            }else Status(404, StatusMessages.NOT_FOUND)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun read(id: String): CommunityDTO? {
        return try{
            return transaction {
                    Community.select { Community.id eq id }.map { mapCommunityDTO(it) }.firstOrNull()
                }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }

    override fun update(communityID: String, name: String?, about: String?, parentID: String?):Status {
        return try{
            if(transaction { !Community.select { Community.id eq communityID }.empty() }){

                transaction {
                    Community.update( { Community.id eq communityID } ){
                        if(!name.isNullOrBlank())
                            it[this.name] = name
                        if(!about.isNullOrBlank())
                            it[this.about] = about
                        if(!parentID.isNullOrBlank() && read(parentID) != null)
                            it[parentCommunityID] = parentID
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

    private fun mapCommunityDTO(it: ResultRow): CommunityDTO {
        return CommunityDTO(name = it[Community.name], id = it[Community.id],
                about = it[Community.imageURL], imageURL =  it[Community.imageURL],
                creationDate = it[Community.creationDate].toString(), location = it[Community.location],
                parentCommunityID = it[Community.parentCommunityID])
    }

}