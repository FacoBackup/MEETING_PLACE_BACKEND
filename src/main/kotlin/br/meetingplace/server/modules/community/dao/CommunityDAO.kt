package br.meetingplace.server.modules.community.dao

import br.meetingplace.server.modules.community.dto.requests.RequestCommunityCreation
import br.meetingplace.server.modules.community.dto.response.CommunityDTO
import br.meetingplace.server.modules.community.entities.Community
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.postgresql.util.PSQLException
import java.util.*

object CommunityDAO: CI {

    override fun create(data: RequestCommunityCreation):HttpStatusCode  {
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
            HttpStatusCode.Created
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override fun check(id: String): Boolean {
        return try{
            !transaction {
                Community.select { Community.id eq id }.empty()
            }
        }catch (normal: Exception){
            false
        }catch (psql: PSQLException){
            false
        }
    }
    override fun delete(id: String): HttpStatusCode {
        return try{
            transaction {
                Community.deleteWhere { Community.id eq id }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override fun read(id: String): CommunityDTO? {
        return try{
            transaction {
                Community.select { Community.id eq id }.map { mapCommunityDTO(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }

    override fun update(communityID: String, name: String?, about: String?, parentID: String?):HttpStatusCode {
        return try{
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
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    private fun mapCommunityDTO(it: ResultRow): CommunityDTO {
        return CommunityDTO(name = it[Community.name], id = it[Community.id],
                about = it[Community.imageURL], imageURL =  it[Community.imageURL],
                creationDate = it[Community.creationDate].toString(), location = it[Community.location],
                parentCommunityID = it[Community.parentCommunityID])
    }

}