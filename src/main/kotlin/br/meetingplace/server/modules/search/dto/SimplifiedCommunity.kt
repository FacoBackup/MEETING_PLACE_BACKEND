package br.meetingplace.server.modules.search.dto

data class SimplifiedCommunity(val name: String, var imageURL: String, var about: String, var moderators: List<String>)