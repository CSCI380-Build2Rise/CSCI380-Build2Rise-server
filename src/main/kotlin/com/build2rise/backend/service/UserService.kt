package com.build2rise.backend.service

import com.build2rise.backend.dto.*
import com.build2rise.backend.entity.Founder
import com.build2rise.backend.entity.Investor
import com.build2rise.backend.repository.FounderRepository
import com.build2rise.backend.repository.InvestorRepository
import com.build2rise.backend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val founderRepository: FounderRepository,
    private val investorRepository: InvestorRepository
) {

    /**
     * Get user profile by ID
     */
    fun getUserProfile(userId: String): UserProfileResponse {
        val userUuid = UUID.fromString(userId)
        val user = userRepository.findById(userUuid).orElseThrow {
            IllegalArgumentException("User not found")
        }

        // Get profile data based on user type
        val profileData = when (user.userType) {
            "founder" -> {
                val founder = founderRepository.findByUserId(userUuid)
                founder?.let {
                    ProfileData(
                        startupName = it.startupName,
                        industry = it.industry,
                        location = it.location,
                        teamSize = it.teamSize,
                        fundingStage = it.fundingStage,
                        description = it.description
                    )
                }
            }
            "investor" -> {
                val investor = investorRepository.findByUserId(userUuid)
                investor?.let {
                    ProfileData(
                        nameFirm = it.nameFirm,
                        industry = it.industry,
                        location = it.geographicPreference,
                        investmentRange = it.investmentRange,
                        fundingStagePreference = it.fundingStagePreference
                    )
                }
            }
            else -> null
        }

        return UserProfileResponse(
            userId = user.id.toString(),
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            userType = user.userType,
            profileImageUrl = user.profileImageUrl,
            createdAt = user.createdAt.toString(),
            profileData = profileData
        )
    }

    /**
     * Create founder profile
     */
    @Transactional
    fun createFounderProfile(userId: String, request: FounderProfileRequest): ProfileData {
        val userUuid = UUID.fromString(userId)

        // Check if user exists and is a founder
        val user = userRepository.findById(userUuid).orElseThrow {
            IllegalArgumentException("User not found")
        }

        if (user.userType != "founder") {
            throw IllegalArgumentException("User is not a founder")
        }

        // Check if profile already exists
        if (founderRepository.findByUserId(userUuid) != null) {
            throw IllegalArgumentException("Founder profile already exists")
        }

        // Create founder profile
        val founder = Founder(
            userId = userUuid,
            startupName = request.startupName,
            industry = request.industry,
            location = request.location,
            teamSize = request.teamSize,
            fundingStage = request.fundingStage,
            description = request.description
        )

        val savedFounder = founderRepository.save(founder)

        return ProfileData(
            startupName = savedFounder.startupName,
            industry = savedFounder.industry,
            location = savedFounder.location,
            teamSize = savedFounder.teamSize,
            fundingStage = savedFounder.fundingStage,
            description = savedFounder.description
        )
    }

    /**
     * Create investor profile
     */
    @Transactional
    fun createInvestorProfile(userId: String, request: InvestorProfileRequest): ProfileData {
        val userUuid = UUID.fromString(userId)

        // Check if user exists and is an investor
        val user = userRepository.findById(userUuid).orElseThrow {
            IllegalArgumentException("User not found")
        }

        if (user.userType != "investor") {
            throw IllegalArgumentException("User is not an investor")
        }

        // Check if profile already exists
        if (investorRepository.findByUserId(userUuid) != null) {
            throw IllegalArgumentException("Investor profile already exists")
        }

        // Create investor profile
        val investor = Investor(
            userId = userUuid,
            nameFirm = request.nameFirm,
            industry = request.industry,
            geographicPreference = request.geographicPreference,
            investmentRange = request.investmentRange,
            fundingStagePreference = request.fundingStagePreference
        )

        val savedInvestor = investorRepository.save(investor)

        return ProfileData(
            nameFirm = savedInvestor.nameFirm,
            industry = savedInvestor.industry,
            location = savedInvestor.geographicPreference,
            investmentRange = savedInvestor.investmentRange,
            fundingStagePreference = savedInvestor.fundingStagePreference
        )
    }
}