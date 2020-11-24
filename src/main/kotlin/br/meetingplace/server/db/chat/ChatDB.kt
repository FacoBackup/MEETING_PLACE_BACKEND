package br.meetingplace.server.db.chat

import br.meetingplace.server.modules.chat.classes.Chat
import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import com.google.gson.GsonBuilder
import java.io.File

object ChatDB: ChatDBInterface{

    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun delete(data: Chat): Status{
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/CHATS/${data.getID()}.json")

        try {
            File(directory).delete()
        } catch (e: Exception) {
            return Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
        return Status(200, StatusMessages.OK)
    }

    override fun select(id: String): Chat? {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/CHATS/$id.json")

        val file = File(directory)
        var chat: Chat? = null
        try {
            val bufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use { it.readText() }
            chat = gson.fromJson(inputString, Chat::class.java)
        } finally {
            return chat
        }
    }

    override fun insert(data: Chat): Status {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/CHATS/${data.getID()}.json")
        try {
            val file = File(directory)
            val json = gson.toJson(data)

            file.writeText(json)
        } catch (e: Exception) {
            return Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
        return Status(200, StatusMessages.OK)
    }

    override fun check(id: String): Boolean {
        return try {
            File(File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/CHATS/${id}.json").exists()
        } catch (e: Exception) {
            false
        }
    }
}