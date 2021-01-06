package br.meetingplace.server.modules.user.dao.user

import br.meetingplace.server.methods.hashString
import br.meetingplace.server.modules.user.dto.requests.RequestUserCreation
import br.meetingplace.server.modules.user.dto.response.UserAuthDTO
import br.meetingplace.server.modules.user.dto.response.UserDTO
import br.meetingplace.server.modules.user.dto.response.UserSimplifiedDTO
import br.meetingplace.server.modules.user.entities.UserEntity
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

object UserDAO: UI {

    override suspend fun create(data: RequestUserCreation): HttpStatusCode {
        return try {
            transaction {
               UserEntity.insert {
                   it[email] = data.email.toLowerCase()
                   it[password] = hashString(encryption = "SHA-1",data.password)
                   it[userName] = data.userName
                   it[gender] = data.gender
                   it[nationality] = data.nationality
                   it[birth] = data.birthDate
                   it[imageURL] = null
                   it[about] = null
                   it[admin] = data.admin
                   it[cityOfBirth] = data.cityOfBirth
                   it[phoneNumber] = data.phoneNumber
               }
            }
            HttpStatusCode.Created
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun delete(userID: String): HttpStatusCode {
        return try {
            transaction {
                UserEntity.deleteWhere { UserEntity.email eq userID }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    override suspend fun readSimplifiedUserByID(userID: String): UserSimplifiedDTO? {
        return try {
            transaction {
                UserEntity.select {
                    UserEntity.email eq userID
                }.map { mapSimplifiedUser(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }
    override suspend fun readByID(userID: String): UserDTO? {
        return try {
            transaction {
                UserEntity.select {
                    UserEntity.email eq userID
                }.map { mapUser(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }

    override suspend fun readByName(name: String, requester: String): List<UserDTO> {
        return try {
            if(name.isNotBlank()){
                transaction {
                    UserEntity.select{
                        (UserEntity.userName like "$name%") and
                                (UserEntity.email neq requester)
                    }.map { mapUser(it) }
                }
            }
            else
                listOf()
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    override suspend fun readAuthUser(userID: String): UserAuthDTO? {
        return try {
            transaction {
                UserEntity.select {
                    UserEntity.email eq userID
                }.map { mapUserAuth(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }
    override suspend fun readAll(): List<UserDTO>{
        return try {
            transaction {
                UserEntity.selectAll().map { mapUser(it) }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }

    override suspend fun check(userID: String): Boolean {
        return try {
            return !transaction {
                UserEntity.select { UserEntity.email eq userID }.empty()
            }
        }catch (normal: Exception){
            false
        }catch (psql: PSQLException){
            false
        }
    }


    override suspend fun readAllByAttribute(
        name: String?,
        birthDate: Long?,
        phoneNumber: String?,
        nationality: String?,
        city: String?
    ): List<UserDTO> {
        return try {
            val users  = mutableListOf<UserDTO>()
            if(!name.isNullOrBlank()) users.add(transaction {
                UserEntity.select {
                    UserEntity.userName eq name
                }.map { mapUser(it) }.first()
            })
            if(birthDate != null) users.addAll(transaction {
                UserEntity.select {
                    UserEntity.birth eq birthDate
                }.map { mapUser(it) }
            })
            if(!phoneNumber.isNullOrBlank()) users.addAll(transaction {
                UserEntity.select {
                    UserEntity.phoneNumber eq phoneNumber
                }.map { mapUser(it) }
            })
            if(!nationality.isNullOrBlank()) users.addAll(transaction {
                UserEntity.select {
                    UserEntity.nationality eq nationality
                }.map { mapUser(it) }
            })

            if(!city.isNullOrBlank()) users.addAll(transaction {
                UserEntity.select {
                    UserEntity.cityOfBirth eq city
                }.map { mapUser(it) }
            })

            users
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }

    override suspend fun update(
        userID: String,
        name: String?,
        imageURL: String?,
        about: String?,
        nationality: String?,
        phoneNumber: String?,
        city: String?,
        backgroundImageURL: String?
    ): HttpStatusCode {
        return try {
            transaction {
                UserEntity.update({UserEntity.email eq userID}){
                    if(!name.isNullOrBlank())
                        it[this.userName] = name
                    if(!about.isNullOrBlank())
                        it[this.about] = about
                    if(!nationality.isNullOrBlank())
                        it[this.nationality] = nationality
                    if(!phoneNumber.isNullOrBlank())
                        it[this.phoneNumber] = phoneNumber
                    if(!city.isNullOrBlank())
                        it[this.cityOfBirth] = city
                    if(!imageURL.isNullOrBlank())
                        it[this.imageURL] = imageURL
                    if(!backgroundImageURL.isNullOrBlank())
                        it[this.backgroundImageURL] = backgroundImageURL
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    private fun mapUserAuth (it: ResultRow): UserAuthDTO{
        return UserAuthDTO(userID = it[UserEntity.email], password = it[UserEntity.password])
    }
    private fun mapSimplifiedUser(it: ResultRow): UserSimplifiedDTO{
        return UserSimplifiedDTO(
            email = it[UserEntity.email],
            name = it[UserEntity.userName],
            birthDate= it[UserEntity.birth],
            imageURL = it[UserEntity.imageURL],
            backgroundImageURL = it[UserEntity.backgroundImageURL])
    }
    private fun mapUser(it: ResultRow): UserDTO {
        return UserDTO(email = it[UserEntity.email],
            name = it[UserEntity.userName],
            gender = it[UserEntity.gender],
            admin = it[UserEntity.admin],
            birthDate = it[UserEntity.birth],
            imageURL = it[UserEntity.imageURL],
            about = it[UserEntity.about],
            cityOfBirth = it[UserEntity.cityOfBirth],
            phoneNumber = it[UserEntity.phoneNumber],
            nationality = it[UserEntity.nationality],
            backgroundImageURL = it[UserEntity.backgroundImageURL]
            )
    }

}