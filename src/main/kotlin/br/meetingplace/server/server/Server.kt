package br.meetingplace.server.server

import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun startServer() = embeddedServer(Netty, System.getenv("PORT")?.toInt() ?: 8080){
    module()
}.start(true)


