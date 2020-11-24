package br.meetingplace.server.db.community

import br.meetingplace.server.modules.community.db.Community
import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import com.google.gson.GsonBuilder
import java.io.File

object CommunityDB: CommunityDBInterface {

    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun delete(data: Community): Status {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/COMMUNITIES/${data.getID()}")
        val file = File(directory)
        try {
            file.delete()
        } catch (e: Exception) {
            return Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
        return Status(200, StatusMessages.OK)
    }

    override fun select(id: String): Community? {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/COMMUNITIES/$id/$id.json")

        val file = File(directory)
        var community: Community? = null
        try {
            val bufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use { it.readText() }
            community = gson.fromJson(inputString, Community::class.java)
        } finally {
            return community
        }
    }

    override fun insert(data: Community): Status{
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/COMMUNITIES/${data.getID()}")
        val jsonFile = "$directory/${data.getID()}.json"
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
            File(File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/COMMUNITIES/${id}.json").exists()
        } catch (e: Exception) {
            false
        }
    }
}