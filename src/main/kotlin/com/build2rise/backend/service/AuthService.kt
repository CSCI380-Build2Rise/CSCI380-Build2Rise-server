package com.build2rise.backend.service

import com.build2rise.backend.dto.*
import com.build2rise.backend.entity.User
import com.build2rise.backend.repository.FounderRepository
import com.build2rise.backend.repository.InvestorRepository
import com.build2rise.backend.repository.UserRepository
import com.build2rise.backend.security.JwtService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val founderRepository: FounderRepository,
    private val investorRepository: InvestorRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {

    /**
     * Register a new user (founder or investor)
     */
    @Transactional
    fun register(request: RegisterRequest): AuthResponse {
        // Check if email already exists
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email already registered")
        }

        // Validate user type
        if (request.userType !in listOf("founder", "investor")) {
            throw IllegalArgumentException("User type must be 'founder' or 'investor'")
        }

        // Create user
        val user = User(
            email = request.email,
            passwordHash = passwordEncoder.encode(request.password),
            firstName = request.firstName,
            lastName = request.lastName,
            userType = request.userType
        )

        val savedUser = userRepository.save(user)

        // Generate JWT token
        val token = jwtService.generateToken(
            userId = savedUser.id.toString(),
            email = savedUser.email,
            userType = savedUser.userType
        )

        return AuthResponse(
            token = token,
            userId = savedUser.id.toString(),
            email = savedUser.email,
            firstName = savedUser.firstName,
            lastName = savedUser.lastName,
            userType = savedUser.userType,
            message = "Registration successful"
        )
    }

    /**
     * Login user
     */
    fun login(request: LoginRequest): AuthResponse {
        // Find user by email
        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("Invalid email or password")

        // Verify password
        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw IllegalArgumentException("Invalid email or password")
        }

        // Generate JWT token
        val token = jwtService.generateToken(
            userId = user.id.toString(),
            email = user.email,
            userType = user.userType
        )

        return AuthResponse(
            token = token,
            userId = user.id.toString(),
            email = user.email,
            firstName = user.firstName,  // Get from user, not request
            lastName = user.lastName,    // Get from user, not request
            userType = user.userType,
            message = "Login successful"
        )
    }
}