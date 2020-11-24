package com.nectarinfotel.data.jsonmodel

data class LoginModel(

    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)