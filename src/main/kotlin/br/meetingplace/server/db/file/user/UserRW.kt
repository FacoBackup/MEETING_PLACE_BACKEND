package br.meetingplace.server.db.file.user

import br.meetingplace.server.db.interfaces.UserDBInterface
import br.meetingplace.server.modules.user.dto.User
import com.google.gson.GsonBuilder
import java.io.File

class UserRW private constructor(): UserDBInterface {
    companion object{
        private val Class = UserRW()
        fun getClass () = Class
    }

    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun delete(data: User) {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/USERS/${data.getEmail()}")
        val filePath = "$directory/${data.getEmail()}.json"

        try {
            File(filePath).delete()
            File(directory).delete()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    override fun select(id: String): User? {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/USERS/$id/$id.json")

        val file = File(directory)
        var user: User? = null
        try {
            val bufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use { it.readText() }
            user = gson.fromJson(inputString, User::class.java)
        } finally {
            return user
        }
    }

    override fun insert(data: User) {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/USERS/${data.getEmail()}")
        val jsonFile = "$directory/${data.getEmail()}.json"
        try {
            val file = File(jsonFile)
            val json = gson.toJson(data)

            if (!File(directory).exists())
                File(directory).mkdir()

            file.writeText(json)
        } catch (e: Exception) {
            println(e.message)
        }
    }
}