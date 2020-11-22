package br.meetingplace.server.db.report.file

import br.meetingplace.server.db.report.ReportDBInterface
import br.meetingplace.server.modules.report.dto.Report
import com.google.gson.GsonBuilder

import java.io.File

class ReportRW private constructor() : ReportDBInterface {
    companion object {
        private val Class = ReportRW()
        fun getClass() = Class
    }

    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun delete(data: Report) {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/REPORTS/${data.reportID}.json")
        val file = File(directory)
        try {
            file.delete()
        } catch (e: Exception) {
            println(e.message)
        }
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

    override fun insert(data: Report) {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/REPORTS/${data.reportID}")
        val jsonFile = "$directory/${data.reportID}.json"
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

    override fun check(id: String): Boolean {
        return try {
            File(File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/REPORTS/${id}.json").exists()
        } catch (e: Exception) {
            false
        }
    }
}