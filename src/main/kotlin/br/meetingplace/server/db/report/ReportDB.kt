package br.meetingplace.server.db.report

import br.meetingplace.server.modules.global.dto.http.status.Status
import br.meetingplace.server.modules.global.dto.http.status.StatusMessages
import br.meetingplace.server.modules.report.classes.Report
import com.google.gson.GsonBuilder
import org.jetbrains.exposed.sql.deleteWhere

import java.io.File

object ReportDB : ReportDBInterface {

    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun delete(data: Report, id: String):Status{
        try {
            data.deleteWhere {id is }
        } catch (e: Exception) {
            return Status(500, StatusMessages.INTERNAL_SERVER_ERROR)
        }
        return Status(200, StatusMessages.OK)
    }

    override fun select(id: String): Report? {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/REPORTS/$id.json")

        val file = File(directory)
        var report: Report? = null
        try {
            val bufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use { it.readText() }
            report = gson.fromJson(inputString, Report::class.java)
        } finally {
            return report
        }
    }

    override fun insert(data: Report):Status {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/REPORTS/${data.reportID}")
        val jsonFile = "$directory/${data.reportID}.json"
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
}