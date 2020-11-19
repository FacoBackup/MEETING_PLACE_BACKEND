package br.meetingplace.server.loadstore.file.report

import br.meetingplace.server.loadstore.interfaces.ReportLSInterface
import br.meetingplace.server.services.community.classes.dependencies.data.Report
import com.google.gson.GsonBuilder

import java.io.File

class ReportRW private constructor(): ReportLSInterface {
    companion object{
        private val Class = ReportRW()
        fun getClass () = Class
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

    override fun load(id: String): Report?{
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

    override fun store(data: Report) {
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
}