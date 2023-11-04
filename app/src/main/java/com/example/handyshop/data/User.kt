package com.example.handyshop.data

data class User(
    var username: String,
    var fullname: String,
    var email: String,
    val password: String
):java.io.Serializable
