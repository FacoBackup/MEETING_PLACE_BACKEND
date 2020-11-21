package br.meetingplace.server.requests.generic.data

data class CreationData(val name: String, val imageURL: String?, val about: String, val identifier: Identifier, val login: Login)