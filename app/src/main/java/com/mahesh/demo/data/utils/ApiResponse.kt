package com.mahesh.demo.data.utils

sealed class ApiResponse<T> {
    data class Success<T>(var data: T) : ApiResponse<T>()
    data class Error<T>(
        val errorMessage: String? = null,
        val errorCode: Int? = null,
    ) : ApiResponse<T>()
    class Loading<T> : ApiResponse<T>()
}