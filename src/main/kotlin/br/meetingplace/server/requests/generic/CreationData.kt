package br.meetingplace.server.requests.generic

data class CreationData(val name: String, val about: String, val identifier: Identifier, val login: Login)