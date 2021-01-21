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
                   it[pic] = null
                   it[background] = null
                   it[about] = null
                   it[cityOfBirth] = data.cityOfBirth
                   it[phoneNumber] = data.phoneNumber
                   it[category] = null
                   it[joinedIn] = System.currentTimeMillis()
               }
            }
            HttpStatusCode.Created
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }

    override suspend fun delete(userID: Long): HttpStatusCode {
        return try {
            transaction {
                UserEntity.deleteWhere { UserEntity.id eq userID }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    override suspend fun readSimplifiedUserByID(userID: Long): UserSimplifiedDTO? {
        return try {
            transaction {
                UserEntity.select {
                    UserEntity.id eq userID
                }.map { mapSimplifiedUser(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }
    override suspend fun readByID(userID: Long): UserDTO? {
        return try {
            transaction {
                UserEntity.select {
                    UserEntity.id eq userID
                }.map { mapUser(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }

    override suspend fun readByName(name: String, requester: Long): List<UserDTO> {
        return try {
            if(name.isNotBlank()){
                transaction {
                    UserEntity.select{
                        (UserEntity.userName like "$name%") and
                                (UserEntity.id neq requester)
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
    override suspend fun readAuthUser(userID: Long): UserAuthDTO? {
        return try {
            transaction {
                UserEntity.select {
                    UserEntity.id eq userID
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

    override suspend fun check(userID: Long): Boolean {
        return try {
            return !transaction {
                UserEntity.select { UserEntity.id eq userID }.empty()
            }
        }catch (normal: Exception){
            false
        }catch (psql: PSQLException){
            false
        }
    }


    override suspend fun readAllByAttribute(
        email: String?,
        name: String?,
        birthDate: Long?,
        phoneNumber: String?,
        nationality: String?,
        city: String?
    ): List<UserDTO> {
        return try {
            val users  = mutableListOf<UserDTO>()
            if(!email.isNullOrBlank()) users.add(transaction {
                UserEntity.select {
                    UserEntity.email eq email
                }.map { mapUser(it) }.first()
            })
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
        userID: Long,
        name: String?,
        imageURL: String?,
        about: String?,
        nationality: String?,
        phoneNumber: String?,
        city: String?,
        backgroundImageURL: String?,
        category: String?
    ): HttpStatusCode {
        return try {
            transaction {
                UserEntity.update({UserEntity.id eq userID}){
                    if(!name.isNullOrBlank())
                        it[this.userName] = name

                    it[this.category] = category

                    it[this.about] = about

                    it[this.nationality] = nationality

                    it[this.phoneNumber] = phoneNumber

                    it[this.cityOfBirth] = city

                    it[this.pic] = imageURL

                    it[this.background] = backgroundImageURL
                }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            println("NORMAL EXCEPTION -> " + normal.message)
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            println("PSQL EXCEPTION -> " + psql.message)
            HttpStatusCode.InternalServerError
        }
    }
    private fun mapUserAuth (it: ResultRow): UserAuthDTO{
        return UserAuthDTO(userID = it[UserEntity.id], password = it[UserEntity.password])
    }
    private fun mapSimplifiedUser(it: ResultRow): UserSimplifiedDTO{
        return UserSimplifiedDTO(
            email = it[UserEntity.email],
            name = it[UserEntity.userName],
            birthDate= it[UserEntity.birth],
            imageURL = it[UserEntity.pic],
            backgroundImageURL = it[UserEntity.background])
    }
    private fun mapUser(it: ResultRow): UserDTO {
        return UserDTO(email = it[UserEntity.email],
            name = it[UserEntity.userName],
            gender = it[UserEntity.gender],
            birthDate = it[UserEntity.birth],
            imageURL = it[UserEntity.pic],
            about = it[UserEntity.about],
            cityOfBirth = it[UserEntity.cityOfBirth],
            phoneNumber = it[UserEntity.phoneNumber],
            nationality = it[UserEntity.nationality],
            backgroundImageURL = it[UserEntity.background],
            joinedIn = it[UserEntity.joinedIn],
            id = it[UserEntity.id]
            )
    }

}