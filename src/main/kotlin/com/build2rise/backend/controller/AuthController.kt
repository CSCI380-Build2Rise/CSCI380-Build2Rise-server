package com.build2rise.backend.controller

import com.build2rise.backend.dto.*
import com.build2rise.backend.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = ["*"])
class AuthController(
    private val authService: AuthService
) {

    /**
     * Register a new user
     * POST /api/auth/register
     */
    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        return try {
            val response = authService.register(request)
            ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                AuthResponse(
                    token = "",
                    userId = "",
                    email = "",
                    firstName = null,
                    lastName = null,
                    userType = "",
                    message = e.message ?: "Registration failed"
                )
            )
        }
    }

    /**
     * Login user
     * POST /api/auth/login
     */
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        return try {
            val response = authService.login(request)
            ResponseEntity.ok(response)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                AuthResponse(
                    token = "",
                    userId = "",
                    email = "",
                    firstName = null,
                    lastName = null,
                    userType = "",
                    message = e.message ?: "Login failed"
                )
            )
        }
    }

    /**
     * Health check endpoint
     * GET /api/auth/health
     */
    @GetMapping("/health")
    fun health(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf(
            "status" to "UP",
            "service" to "Build2Rise Auth API",
            "timestamp" to System.currentTimeMillis().toString()
        ))
    }
}