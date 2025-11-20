package com.build2rise.backend.controller

import com.build2rise.backend.dto.*
import com.build2rise.backend.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = ["*"])
class UserController(
    private val userService: UserService
) {

    /**
     * Get current user's profile
     * GET /api/users/profile
     */
    @GetMapping("/profile")
    fun getCurrentUserProfile(authentication: Authentication): ResponseEntity<UserProfileResponse> {
        return try {
            val userId = authentication.principal as String
            val profile = userService.getUserProfile(userId)
            ResponseEntity.ok(profile)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    /**
     * Get user profile by ID
     * GET /api/users/{userId}
     */
    @GetMapping("/{userId}")
    fun getUserProfile(@PathVariable userId: String): ResponseEntity<UserProfileResponse> {
        return try {
            val profile = userService.getUserProfile(userId)
            ResponseEntity.ok(profile)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    /**
     * Create founder profile
     * POST /api/users/founder-profile
     */
    @PostMapping("/founder-profile")
    fun createFounderProfile(
        authentication: Authentication,
        @Valid @RequestBody request: FounderProfileRequest
    ): ResponseEntity<ProfileData> {
        return try {
            val userId = authentication.principal as String
            val profile = userService.createFounderProfile(userId, request)
            ResponseEntity.status(HttpStatus.CREATED).body(profile)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    /**
     * Create investor profile
     * POST /api/users/investor-profile
     */
    @PostMapping("/investor-profile")
    fun createInvestorProfile(
        authentication: Authentication,
        @Valid @RequestBody request: InvestorProfileRequest
    ): ResponseEntity<ProfileData> {
        return try {
            val userId = authentication.principal as String
            val profile = userService.createInvestorProfile(userId, request)
            ResponseEntity.status(HttpStatus.CREATED).body(profile)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    /**
     * Search users for connections
     * GET /api/users/search?userType=founder&industry=HealthTech&location=NY
     */
    @GetMapping("/search")
    fun searchUsers(
        authentication: Authentication,
        @RequestParam(required = false) userType: String?,
        @RequestParam(required = false) industry: String?,
        @RequestParam(required = false) location: String?,
        @RequestParam(required = false) fundingStage: String?
    ): ResponseEntity<UserSearchResponse> {
        val currentUserId = authentication.principal as String
        val results = userService.searchUsers(
            currentUserId = currentUserId,
            userType = userType,
            industry = industry,
            location = location,
            fundingStage = fundingStage
        )
        return ResponseEntity.ok(results)
    }
}