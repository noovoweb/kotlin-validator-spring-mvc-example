package com.noovoweb.project.response

data class ErrorsResponse(
    val message: String,
    val errors: Map<String, List<String>>
)
