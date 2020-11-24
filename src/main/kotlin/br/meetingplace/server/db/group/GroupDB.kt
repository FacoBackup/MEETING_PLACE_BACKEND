package br.meetingplace.server.db.group

import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.modules.groups.db.Group
import com.google.gson.GsonBuilder
import java.io.File

object GroupDB : GroupDBInterface {


    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun delete(data: Group):Status {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/GROUPS/${data.getID()}.json")
        val file = File(directory)
        try {
            file.delete()
        } catch (e: Exception) {
            return Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
        return Status(200, StatusMessages.OK)
    }

    override fun select(id: String): Group? {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/GROUPS/$id.json")

        val file = File(directory)
        var group: Group? = null
        try {
            val bufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use { it.readText() }
            group = gson.fromJson(inputString, Group::class.java)
        } finally {
            return group
        }
    }

    override fun insert(data: Group): Status{
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/GROUPS/${data.getID()}.json")

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
            File(File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/GROUPS/${id}.json").exists()
        } catch (e: Exception) {
            false
        }
    }
}