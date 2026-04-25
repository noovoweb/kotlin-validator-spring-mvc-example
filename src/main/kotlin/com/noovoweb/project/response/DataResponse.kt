package com.noovoweb.project.response

data class DataResponse<T>(
    val data: T,
    val message: String
)
