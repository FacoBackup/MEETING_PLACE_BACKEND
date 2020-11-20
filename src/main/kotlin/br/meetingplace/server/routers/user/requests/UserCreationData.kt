package br.meetingplace.server.routers.user.requests

data class UserCreationData(val userName: String, val age: Int, val email: String, val password: String)