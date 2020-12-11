package br.meetingplace.server.modules.test

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.testing(){
    route("/api"){
        post<Test>("/post/test"){
            println("POST Requested.")
            println(it)
            call.respond(HttpStatusCode.OK.toString())
        }
        get ("/get/test"){
            println("GET Requested.")
            call.respond(Test("My name here.", email = "gustavo@gmail.com"))
        }
        post ("/post/user/test"){
            val data = call.receive<Test>()
            println("POST User Requested.")
            println("result: $data")
            if(data.email == "gustavo@gmail.com")
                call.respond(Test("Joao", email = "joao@gmail.com"))
            else
                call.respond(HttpStatusCode.NotFound)
        }
        put ("/put/test"){
            val data = call.receive<Test>()
            println("PUT Requested.")
            println(data)
            data.email = "${data.email} ok"
            data.name = "${data.name} ok"
            call.respond(data)
        }
        patch ("/patch/test"){
            val data = call.receive<Test>()

            println("PATCH Requested.")
            println(data)
            data.email = "${data.email} ok"
            call.respond(data)
        }
    }
}