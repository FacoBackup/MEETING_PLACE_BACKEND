package br.meetingplace.server.user.classes

import br.meetingplace.server.user.classes.dependencies.Controller

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