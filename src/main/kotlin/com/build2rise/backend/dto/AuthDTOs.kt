package com.build2rise.backend.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

// Registration Request
data class RegisterRequest(
    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email is required")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, message = "Password must be at least 6 characters")
    val password: String,

    @field:NotBlank(message = "First name is required")
    val firstName: String,

    @field:NotBlank(message = "Last name is required")
    val lastName: String,

    @field:NotBlank(message = "User type is required")
    val userType: String // "founder" or "investor"
)

// Login Request
data class LoginRequest(
    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email is required")
    val email: String,

    @field:NotBlank(message = "Password is required")
    val password: String
)

// Auth Response (after successful login/register)
data class AuthResponse(
    val token: String,
    val userId: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val userType: String,
    val message: String = "Success"
)

// Error Response
data class ErrorResponse(
    val message: String,
    val status: Int,
    val timestamp: Long = System.currentTimeMillis()
)