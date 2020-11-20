package br.meetingplace.server.routers.generic.requests

data class Identifier(val ID: String, val owner: String?, val community: Boolean)