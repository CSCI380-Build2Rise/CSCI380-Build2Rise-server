package com.build2rise.backend.dto

import java.util.UUID

data class FounderMatchDTO(
    val founderId: UUID,
    val founderUserId: UUID,
    val startupName: String,
    val industry: String,
    val location: String,
    val fundingStage: String,
    val description: String,
    val teamSize: String?,
    val matchScore: Int,
    val matchReasons: List<String>
)

data class InvestorMatchDTO(
    val investorId: UUID,
    val investorUserId: UUID,
    val nameFirm: String,
    val industry: String,
    val geographicPreference: String,
    val fundingStagePreference: String,
    val investmentRange: String,
    val matchScore: Int,
    val matchReasons: List<String>
)

data class MatchResult(
    val matches: List<Any>,
    val totalMatches: Int,
    val averageScore: Double
)