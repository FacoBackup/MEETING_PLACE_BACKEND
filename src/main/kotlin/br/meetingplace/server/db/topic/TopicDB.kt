package br.meetingplace.server.db.topic

import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.modules.topic.db.Topic
import com.google.gson.GsonBuilder
import java.io.File

object TopicDB : TopicDBInterface {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun delete(data: Topic): Status{
        var directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/TOPICS")
        directory += if (data.getMainTopic().isNullOrBlank())
            "/${data.getID()}"
        else
            "/${data.getMainTopic()}"

        try {
            File(directory + "/${data.getID()}.json").delete()
        } catch (e: Exception) {
            return Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
        return Status(200, StatusMessages.OK)
    }

    override fun select(id: String, mainTopic: String?): Topic? {
        var directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/TOPICS")
        directory += if (mainTopic.isNullOrBlank())
            "/$id/$id.json"
        else
            "/$mainTopic/$id.json"

        val file = File(directory)
        var topic: Topic? = null
        try {
            val bufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use { it.readText() }
            topic = gson.fromJson(inputString, Topic::class.java)
        } finally {
            return topic
        }
    }

    override fun insert(data: Topic):Status {
        var directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/TOPICS/${data.getID()}.json")
        try {
            val json = gson.toJson(data)
            if (!File(directory).exists())
                File(directory).mkdir()

            File(directory).writeText(json)
        } catch (e: Exception) {
            return Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
        return Status(200, StatusMessages.OK)
    }

    override fun check(id: String): Boolean {
        return try {
            File(File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/TOPICS/${id}.json").exists()
        } catch (e: Exception) {
            false
        }
    }
}