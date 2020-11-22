package br.meetingplace.server.db.group.file

import br.meetingplace.server.db.group.GroupDBInterface
import br.meetingplace.server.modules.groups.dto.Group
import com.google.gson.GsonBuilder
import java.io.File

class GroupRW private constructor() : GroupDBInterface {
    companion object {
        private val Class = GroupRW()
        fun getClass() = Class
    }

    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun delete(data: Group) {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/GROUPS/${data.getGroupID()}.json")
        val file = File(directory)
        try {
            file.delete()
        } catch (e: Exception) {
            println(e.message)
        }
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

    override fun insert(data: Group) {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/GROUPS/${data.getGroupID()}.json")

        try {
            val file = File(directory)
            val json = gson.toJson(data)

            file.writeText(json)
        } catch (e: Exception) {
            println(e.message)
        }
    }

    override fun check(id: String): Boolean {
        return try {
            File(File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/GROUPS/${id}.json").exists()
        } catch (e: Exception) {
            false
        }
    }
}