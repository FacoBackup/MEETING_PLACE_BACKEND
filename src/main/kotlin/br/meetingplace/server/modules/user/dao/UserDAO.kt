package br.meetingplace.server.modules.user.dao

import br.meetingplace.server.modules.user.dto.response.UserDTO
import br.meetingplace.server.modules.user.entities.User
import br.meetingplace.server.modules.user.dto.requests.RequestUserCreation
import br.meetingplace.server.response.status.Status
import br.meetingplace.server.response.status.StatusMessages
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.format.DateTimeFormat
import org.postgresql.util.PSQLException
import java.util.*

object UserDAO: UI {
    override fun create(data: RequestUserCreation): Status {
        return try {
            transaction {
                User.insert {
                    it[id] = UUID.randomUUID().toString()
                    it[userName] = data.userName
                    it[email] = data.email
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
                User.deleteWhere { User.id eq userID }
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
                    User.id eq userID
                }.map { mapUser(it) }.firstOrNull()
            }
        }catch (normal: Exception){
            null
        }catch (psql: PSQLException){
            null
        }
    }

    override fun readAll(
        name: String?,
        birthDate: String?,
        phoneNumber: String?,
        nationality: String?,
        city: String?
    ): List<UserDTO> {
        return try {
            val users  = mutableListOf<UserDTO>()
            if(!name.isNullOrBlank()) users.addAll(transaction {
                User.select {
                    User.userName eq name
                }.map { mapUser(it) }
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
            if(!name.isNullOrBlank()) users.addAll(transaction {
                User.select {
                    User.userName eq name
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
                User.update({User.id eq userID}){
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
        return UserDTO(id = it[User.id], name = it[User.userName],
            email = it[User.email], gender = it[User.gender],
            birthDate =  it[User.birth].toString(), imageURL = it[User.imageURL],
            about = it[User.about], cityOfBirth = it[User.cityOfBirth],
            phoneNumber = it[User.phoneNumber], nationality = it[User.nationality])
    }

}