package br.meetingplace.server.loadstore.file.chat

import br.meetingplace.server.loadstore.interfaces.ChatLSInterface
import br.meetingplace.server.services.chat.classes.Chat
import com.google.gson.GsonBuilder
import java.io.File

class ChatRW private constructor() : ChatLSInterface {
    companion object{
        private val Class = ChatRW()
        fun getClass () = Class
    }

    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun delete(data: Chat) {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/CHATS/${data.getID()}.json")

        try {
            File(directory).delete()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    override fun load(id: String): Chat?{
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

    override fun store(data: Chat) {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/CHATS/${data.getID()}.json")
        try {
            val file = File(directory)
            val json = gson.toJson(data)

            file.writeText(json)
        } catch (e: Exception) {
            println(e.message)
        }
    }
}