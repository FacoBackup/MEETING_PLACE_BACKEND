package br.meetingplace.server.routers.generic.requests

data class CreationData(val name: String, val about: String, val identifier: Identifier, val login: Login)