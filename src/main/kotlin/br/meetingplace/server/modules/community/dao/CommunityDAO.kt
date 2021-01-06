package br.meetingplace.server.modules.community.dao

import br.meetingplace.server.modules.community.dto.requests.RequestCommunityCreation
import br.meetingplace.server.modules.community.dto.response.CommunityDTO
import br.meetingplace.server.modules.community.entities.CommunityEntity
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
                CommunityEntity.insert {
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
                CommunityEntity.select { CommunityEntity.id eq id }.empty()
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
                CommunityEntity.deleteWhere { CommunityEntity.id eq id }
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
                CommunityEntity.select { CommunityEntity.id eq id }.map { mapCommunityDTO(it) }.firstOrNull()
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
                CommunityEntity.update( { CommunityEntity.id eq communityID } ){
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
        return CommunityDTO(name = it[CommunityEntity.name], id = it[CommunityEntity.id],
                about = it[CommunityEntity.imageURL], imageURL =  it[CommunityEntity.imageURL],
                creationDate = it[CommunityEntity.creationDate].toString(), location = it[CommunityEntity.location],
                parentCommunityID = it[CommunityEntity.parentCommunityID])
    }

}