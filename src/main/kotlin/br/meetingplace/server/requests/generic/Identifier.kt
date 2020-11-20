package br.meetingplace.server.requests.generic

data class Identifier(val ID: String, val owner: String?, val community: Boolean)