package br.meetingplace.server.requests.users

data class UserCreationData(val userName: String, val age: Int, val email: String, val password: String)