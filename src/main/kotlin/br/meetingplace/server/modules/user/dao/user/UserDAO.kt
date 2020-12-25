package br.meetingplace.server.modules.user.dao.user

import br.meetingplace.server.methods.hashString
import br.meetingplace.server.modules.user.dto.requests.RequestUserCreation
import br.meetingplace.server.modules.user.dto.response.UserAuthDTO
import br.meetingplace.server.modules.user.dto.response.UserDTO
import br.meetingplace.server.modules.user.dto.response.UserSocialDTO
import br.meetingplace.server.modules.user.entities.User
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.postgresql.util.PSQLException
import java.text.DateFormat

object UserDAO: UI {

    override suspend fun create(data: RequestUserCreation): HttpStatusCode {
        return try {
            transaction {
               User.insert {
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
                User.deleteWhere { User.email eq userID }
            }
            HttpStatusCode.OK
        }catch (normal: Exception){
            HttpStatusCode.InternalServerError
        }catch (psql: PSQLException){
            HttpStatusCode.InternalServerError
        }
    }
    override suspend fun readSocialByID(userID: String): UserSocialDTO? {
        return try {
            transaction {
                User.select {
                    User.email eq userID
                }.map { mapUserSocial(it) }.firstOrNull()
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
                User.select {
                    User.email eq userID
                }.map { mapUser(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }

    override suspend fun readByName(name: String): List<UserDTO> {
        return try {
            transaction {
                User.select{
                    User.userName like "$name%"
                }.map { mapUser(it) }
            }
        }catch (normal: Exception){
            listOf()
        }catch (psql: PSQLException){
            listOf()
        }
    }
    override suspend fun readAuthUser(userID: String): UserAuthDTO? {
        return try {
            transaction {
                User.select {
                    User.email eq userID
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
                User.selectAll().map { mapUser(it) }
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
                User.select { User.email eq userID }.empty()
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
                User.select {
                    User.userName eq name
                }.map { mapUser(it) }.first()
            })
            if(birthDate != null) users.addAll(transaction {
                User.select {
                    User.birth eq birthDate
                }.map { mapUser(it) }
            })
            if(!phoneNumber.isNullOrBlank()) users.addAll(transaction {
                User.select {
                    User.phoneNumber eq phoneNumber
                }.map { mapUser(it) }
            })
            if(!nationality.isNullOrBlank()) users.addAll(transaction {
                User.select {
                    User.nationality eq nationality
                }.map { mapUser(it) }
            })

            if(!city.isNullOrBlank()) users.addAll(transaction {
                User.select {
                    User.cityOfBirth eq city
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
        city: String?
    ): HttpStatusCode {
        return try {
            transaction {
                User.update({User.email eq userID}){
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
        return UserAuthDTO(userID = it[User.email], password = it[User.password])
    }
    private fun mapUserSocial(it: ResultRow): UserSocialDTO{
        return UserSocialDTO(
            email = it[User.email],
            name = it[User.userName],
            bornDate= it[User.birth],
            imageURL = it[User.imageURL])

    }
    private fun mapUser(it: ResultRow): UserDTO {
        return UserDTO(email = it[User.email], name = it[User.userName],
            gender = it[User.gender], admin = it[User.admin],
            birthDate = (it[User.birth].toString()).replaceAfter("T", "").removeSuffix("T"), imageURL = it[User.imageURL],
            about = it[User.about], cityOfBirth = it[User.cityOfBirth],
            phoneNumber = it[User.phoneNumber], nationality = it[User.nationality])
    }

}