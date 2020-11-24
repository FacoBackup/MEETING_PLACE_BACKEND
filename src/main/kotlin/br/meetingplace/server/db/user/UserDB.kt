package br.meetingplace.server.db.user

import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.modules.user.db.User
import com.google.gson.GsonBuilder
import java.io.File

class UserDB private constructor() : UserDBInterface {
    companion object {
        private val Class = UserDB()
        fun getClass() = Class
    }

    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun delete(data: User): Status {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/USERS/${data.getEmail()}")
        val filePath = "$directory/${data.getEmail()}.json"

        try {
            File(filePath).delete()
            File(directory).delete()
        } catch (e: Exception) {
            return Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
        return Status(200, StatusMessages.OK)
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

    override fun insert(data: User): Status {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/USERS/${data.getEmail()}")
        val jsonFile = "$directory/${data.getEmail()}.json"
        try {
            val file = File(jsonFile)
            val json = gson.toJson(data)

            if (!File(directory).exists())
                File(directory).mkdir()

            file.writeText(json)
        } catch (e: Exception) {
            return Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
        return Status(200, StatusMessages.OK)
    }

    override fun check(id: String): Boolean {
        return try {
            File(File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/USERS/${id}.json").exists()
        } catch (e: Exception) {
            false
        }
    }
}