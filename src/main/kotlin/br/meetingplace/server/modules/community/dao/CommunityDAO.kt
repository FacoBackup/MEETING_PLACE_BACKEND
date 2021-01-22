package br.meetingplace.server.modules.community.dao

import br.meetingplace.server.modules.community.dto.requests.RequestCommunityCreation
import br.meetingplace.server.modules.community.dto.response.CommunityDTO
import br.meetingplace.server.modules.community.entities.CommunityEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object CommunityDAO: CI {

    override suspend fun create(data: RequestCommunityCreation):HttpStatusCode  {
        return try{
            transaction {
                CommunityEntity.insert {
                    it[name] = data.name

                    it[pic] = data.pic
                    it[creationDate] =  System.currentTimeMillis()
                    it[about] = data.about
                    it[mainCommunityID]= data.relatedCommunityID
                    it[background] = data.background
                }
            }
            HttpStatusCode.Created
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun readRelatedCommunities(id: Long): List<CommunityDTO> {
        return try{
            transaction {
                CommunityEntity.select{
                    CommunityEntity.mainCommunityID eq id
                }.map { mapCommunityDTO(it) }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    override suspend fun check(id: Long): Boolean {
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
    override suspend fun delete(id: Long): HttpStatusCode {
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

    override suspend fun read(id: Long): CommunityDTO? {
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
    override suspend fun update(communityID: Long, imageURL: String?, backgroundImageURL: String?, about: String?):HttpStatusCode {
        return try{
            transaction {
                CommunityEntity.update( { CommunityEntity.id eq communityID } ){
                    if(!about.isNullOrBlank())
                        it[this.about] = about
                    if(!backgroundImageURL.isNullOrBlank())
                        it[this.background] = backgroundImageURL
                    if(!imageURL.isNullOrBlank())
                        it[this.pic] = imageURL
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
            about = it[CommunityEntity.about],
            imageURL =  it[CommunityEntity.pic],
            creationDate = it[CommunityEntity.creationDate],
            parentCommunityID = it[CommunityEntity.mainCommunityID],
            backgroundImageURL = it[CommunityEntity.background]
            )
    }

}