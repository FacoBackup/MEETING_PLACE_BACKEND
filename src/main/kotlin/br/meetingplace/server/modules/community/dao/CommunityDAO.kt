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

    override suspend fun create(data: RequestCommunityCreation):HttpStatusCode  {
        return try{
            transaction {
                CommunityEntity.insert {
                    it[name] = data.name
                    it[id] = UUID.randomUUID().toString()
                    it[imageURL] = data.imageURL
                    it[creationDate] =  System.currentTimeMillis()
                    it[about] = data.about
                    it[parentCommunityID]= data.parentCommunityID
                    it[backgroundImageURL] = data.backgroundImageURL
                }
            }
            HttpStatusCode.Created
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun readParentCommunities(communityID: String): List<CommunityDTO> {
        return try{
            transaction {
                CommunityEntity.select{
                    CommunityEntity.parentCommunityID eq communityID
                }.map { mapCommunityDTO(it) }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    override suspend fun check(id: String): Boolean {
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
    override suspend fun delete(id: String): HttpStatusCode {
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

    override suspend fun read(id: String): CommunityDTO? {
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

    override suspend fun readByExactName(name: String): CommunityDTO? {
        return try{
            transaction {
                CommunityEntity.select{
                    CommunityEntity.name eq name
                }.map { mapCommunityDTO(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }

    override suspend fun readByName(name: String): List<CommunityDTO> {
        return try{
            transaction {
                CommunityEntity.select{
                    CommunityEntity.name like "%$name%"
                }.map { mapCommunityDTO(it) }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    override suspend fun update(communityID: String, name: String?, imageURL: String?, backgroundImageURL: String?,about: String?, parentID: String?):HttpStatusCode {
        return try{
            transaction {
                CommunityEntity.update( { CommunityEntity.id eq communityID } ){
                    if(!name.isNullOrBlank())
                        it[this.name] = name
                    if(!about.isNullOrBlank())
                        it[this.about] = about
                    if(!parentID.isNullOrBlank())
                        it[parentCommunityID] = parentID
                    if(!backgroundImageURL.isNullOrBlank())
                        it[this.backgroundImageURL] = backgroundImageURL
                    if(!imageURL.isNullOrBlank())
                        it[this.imageURL] = imageURL
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
        return CommunityDTO(
            name = it[CommunityEntity.name],
            id = it[CommunityEntity.id],
            about = it[CommunityEntity.imageURL],
            imageURL =  it[CommunityEntity.imageURL],
            creationDate = it[CommunityEntity.creationDate],
            parentCommunityID = it[CommunityEntity.parentCommunityID],
            backgroundImageURL = it[CommunityEntity.backgroundImageURL]
            )
    }

}