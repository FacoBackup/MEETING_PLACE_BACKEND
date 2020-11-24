package br.meetingplace.server.modules.chat.classes

import br.meetingplace.server.modules.global.dto.owner.OwnerData
import org.jetbrains.exposed.sql.Table
import java.time.LocalDateTime

class Chat(private val id: String, private val owner: OwnerData,private val creationDate: LocalDateTime): Table() {
    fun getCreationDate () = creationDate
    fun getOwner() = owner
    fun getID() = id
}