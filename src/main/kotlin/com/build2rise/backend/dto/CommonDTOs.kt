package com.build2rise.backend.dto


// Generic Success Response
data class SuccessResponse(
    val message: String,
    val data: Any? = null,
    val timestamp: Long = System.currentTimeMillis()
)

// Generic Error Response
data class ApiErrorResponse(
    val error: String,
    val message: String,
    val status: Int,
    val timestamp: Long = System.currentTimeMillis()
)

// Pagination Response
data class PageResponse<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val last: Boolean
)