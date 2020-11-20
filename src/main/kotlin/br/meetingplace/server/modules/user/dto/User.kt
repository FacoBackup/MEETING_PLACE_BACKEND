package br.meetingplace.server.modules.user.dto

import br.meetingplace.server.modules.user.dto.dependencies.Controller

class User(
        private var userName: String,
        private var age: Int,
        private var email: String,
        private var password: String
) : Controller() {
    fun getPassword() = password
    fun getAge() = age
    fun getEmail() = email
    fun getUserName() = userName
}