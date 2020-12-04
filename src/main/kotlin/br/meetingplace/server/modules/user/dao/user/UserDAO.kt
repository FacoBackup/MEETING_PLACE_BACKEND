package br.meetingplace.server.modules.user.dao.user

import br.meetingplace.server.modules.user.dto.response.UserDTO
import br.meetingplace.server.modules.user.entities.User
import br.meetingplace.server.modules.user.dto.requests.RequestUserCreation
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.format.DateTimeFormat
import org.postgresql.util.PSQLException
import java.security.MessageDigest
import kotlin.text.Charsets.UTF_8

object UserDAO: UI {
    override fun create(data: RequestUserCreation): Status {
        return try {
            transaction {
                User.insert {
                    it[email] = data.email
                    it[password] = MessageDigest.getInstance("MD5").digest(data.password.toByteArray(UTF_8)).toString()
                    it[userName] = data.userName
                    it[gender] = data.gender
                    it[nationality] = data.nationality
                    it[birth] = DateTimeFormat.forPattern("dd-MM-yyyy").parseDateTime(data.birthDate)
                    it[imageURL] = null
                    it[about] = null
                    it[cityOfBirth] = data.cityOfBirth
                    it[phoneNumber] = data.phoneNumber
                }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun delete(userID: String): Status {
        return try {
            transaction {
                User.deleteWhere { User.email eq userID }
            }
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }

    override fun read(userID: String): UserDTO? {
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
    override fun readAll(): List<UserDTO>{
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
    override fun readAllByAttribute(
        name: String?,
        birthDate: String?,
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
            if(!birthDate.isNullOrBlank()) users.addAll(transaction {
                User.select {
                    User.birth eq DateTimeFormat.forPattern("dd-MM-yyyy").parseDateTime(birthDate)
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

    override fun update(
        userID: String,
        name: String?,
        imageURL: String?,
        about: String?,
        nationality: String?,
        phoneNumber: String?,
        city: String?
    ): Status {
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
            Status(200, StatusMessages.OK)
        }catch (normal: Exception){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }catch (psql: PSQLException){
            Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
    }
    private fun mapUser(it: ResultRow): UserDTO {
        return UserDTO(email = it[User.email], name = it[User.userName],
            gender = it[User.gender], password = it[User.password],
            birthDate =  it[User.birth].toString(), imageURL = it[User.imageURL],
            about = it[User.about], cityOfBirth = it[User.cityOfBirth],
            phoneNumber = it[User.phoneNumber], nationality = it[User.nationality])
    }

}