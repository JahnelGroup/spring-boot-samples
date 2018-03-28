package com.example.api.errors

import org.springframework.http.HttpStatus

data class ApiError(
    var status: HttpStatus,
    var clientMessage: String,
    var errors: List<String>,
    var developerMessage: String? = null
){
    constructor(status: HttpStatus, clientMessage: String, error: String, developerMessage: String? = null) :
        this(status, clientMessage, arrayListOf<String>(error), developerMessage)
}