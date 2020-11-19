package br.meetingplace.server.loadstore.file.group

import br.meetingplace.server.loadstore.interfaces.GroupLSInterface
import br.meetingplace.server.services.groups.classes.Group
import com.google.gson.GsonBuilder
import java.io.File

class GroupRW private constructor(): GroupLSInterface {
    companion object{
        private val Class = GroupRW()
        fun getClass () = Class
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

    override fun read(id: String): Group?{
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

    override fun write(data: Group) {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/GROUPS/${data.getGroupID()}.json")

        try {
            val file = File(directory)
            val json = gson.toJson(data)

            file.writeText(json)
        } catch (e: Exception) {
            println(e.message)
        }
    }
}