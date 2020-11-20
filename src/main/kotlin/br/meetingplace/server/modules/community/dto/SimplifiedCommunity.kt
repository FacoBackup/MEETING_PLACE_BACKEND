package br.meetingplace.server.modules.community.dto

data class SimplifiedCommunity(val name: String, var imageURL: String,var about: String, var moderators: List<String>)