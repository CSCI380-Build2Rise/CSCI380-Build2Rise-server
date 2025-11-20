package com.build2rise.backend.dto

// Founder Profile Request
data class FounderProfileRequest(
    val startupName: String,
    val industry: String?,
    val location: String?,
    val teamSize: String?,
    val fundingStage: String?,
    val description: String?
)

// Investor Profile Request
data class InvestorProfileRequest(
    val nameFirm: String,
    val industry: String?,
    val geographicPreference: String?,
    val investmentRange: String?,
    val fundingStagePreference: String?
)

// User Profile Response
data class UserProfileResponse(
    val userId: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val userType: String,
    val profileImageUrl: String?,
    val createdAt: String,
    val profileData: ProfileData?
)

// Profile Data (can be Founder or Investor data)
data class ProfileData(
    // Founder fields
    val startupName: String? = null,
    val teamSize: String? = null,
    val fundingStage: String? = null,

    // Investor fields
    val nameFirm: String? = null,
    val investmentRange: String? = null,
    val fundingStagePreference: String? = null,

    // Common fields
    val industry: String? = null,
    val location: String? = null,
    val description: String? = null
)

// User Search Response
data class UserSearchResponse(
    val users: List<UserProfileResponse>,
    val totalCount: Int
)


// Support Project Request
data class SupportProjectRequest(
    val founderUserId: String,
    val status: String = "interested"
)

// Update Project Status Request
data class UpdateProjectStatusRequest(
    val status: String
)

// Project Response
data class ProjectResponse(
    val id: String,
    val founderId: String,
    val founderName: String?,
    val founderStartupName: String?,
    val founderIndustry: String?,
    val investorId: String,
    val investorName: String?,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)

// List of Projects Response
data class ProjectsListResponse(
    val projects: List<ProjectResponse>,
    val totalCount: Int
)