package com.build2rise.backend.service

import com.build2rise.backend.dto.*
import com.build2rise.backend.entity.Founder
import com.build2rise.backend.entity.Investor
import com.build2rise.backend.repository.FounderRepository
import com.build2rise.backend.repository.InvestorRepository
import com.build2rise.backend.repository.ProjectRepository
import com.build2rise.backend.repository.ConnectionRepository
import org.springframework.stereotype.Service
import java.util.*

data class MatchScore(
    var score: Int,
    val reasons: MutableList<String> = mutableListOf()
)

@Service
class MatchingService(
    private val founderRepository: FounderRepository,
    private val investorRepository: InvestorRepository,
    private val projectRepository: ProjectRepository,
    private val connectionRepository: ConnectionRepository,
    private val geminiService: GeminiMatchingService // ✅ ADD THIS
) {

    /**
     * Find top matches for an investor (returns founders they haven't supported yet)
     */
    fun findMatchesForInvestor(investorId: UUID, limit: Int = 20): MatchResult {
        val investor = investorRepository.findById(investorId)
            .orElseThrow { IllegalArgumentException("Investor not found") }

        val supportedFounderIds = projectRepository.findByInvestorId(investorId)
            .map { it.founderId }
            .toSet()

        val allFounders = founderRepository.findAll()
            .filter { it.hasRequiredFields() }
            .filter { it.id !in supportedFounderIds }

        // ✅ CHANGED: Use Gemini for scoring instead of rule-based
        val scoredMatches = allFounders
            .map { founder ->
                val score = calculateFounderScoreWithAI(investor, founder)
                Pair(founder, score)
            }
            .filter { it.second.score >= 30 }
            .sortedByDescending { it.second.score }
            .take(limit)

        val matches = scoredMatches.map { (founder, scoreResult) ->
            FounderMatchDTO(
                founderId = founder.id!!,
                founderUserId = founder.userId,
                startupName = founder.startupName,
                industry = founder.industry ?: "",
                location = founder.location ?: "",
                fundingStage = founder.fundingStage ?: "",
                description = founder.description ?: "",
                teamSize = founder.teamSize,
                matchScore = scoreResult.score,
                matchReasons = scoreResult.reasons
            )
        }

        val averageScore = if (matches.isNotEmpty()) {
            matches.map { it.matchScore }.average()
        } else {
            0.0
        }

        return MatchResult(
            matches = matches,
            totalMatches = matches.size,
            averageScore = averageScore
        )
    }

    /**
     * Find top matches for a founder (returns investors they haven't connected with yet)
     */
    fun findMatchesForFounder(founderId: UUID, limit: Int = 20): MatchResult {
        val founder = founderRepository.findById(founderId)
            .orElseThrow { IllegalArgumentException("Founder not found") }

        val connectedInvestorUserIds = connectionRepository.findByUser1IdOrUser2Id(founder.userId, founder.userId)
            .map { connection ->
                if (connection.user1Id == founder.userId) connection.user2Id else connection.user1Id
            }
            .toSet()

        val allInvestors = investorRepository.findAll()
            .filter { it.hasRequiredFields() }
            .filter { it.userId !in connectedInvestorUserIds }

        // ✅ CHANGED: Use Gemini for scoring instead of rule-based
        val scoredMatches = allInvestors
            .map { investor ->
                val score = calculateInvestorScoreWithAI(founder, investor)
                Pair(investor, score)
            }
            .filter { it.second.score >= 30 }
            .sortedByDescending { it.second.score }
            .take(limit)

        val matches = scoredMatches.map { (investor, scoreResult) ->
            InvestorMatchDTO(
                investorId = investor.id!!,
                investorUserId = investor.userId,
                nameFirm = investor.nameFirm,
                industry = investor.industry ?: "",
                geographicPreference = investor.geographicPreference ?: "",
                fundingStagePreference = investor.fundingStagePreference ?: "",
                investmentRange = investor.investmentRange ?: "",
                matchScore = scoreResult.score,
                matchReasons = scoreResult.reasons
            )
        }

        val averageScore = if (matches.isNotEmpty()) {
            matches.map { it.matchScore }.average()
        } else {
            0.0
        }

        return MatchResult(
            matches = matches,
            totalMatches = matches.size,
            averageScore = averageScore
        )
    }

    // ✅ NEW: AI-powered scoring for founders
    private fun calculateFounderScoreWithAI(investor: Investor, founder: Founder): MatchScore {
        val founderProfile = """
        Startup: ${founder.startupName}
        Industry: ${founder.industry ?: "Not specified"}
        Location: ${founder.location ?: "Not specified"}
        Team Size: ${founder.teamSize ?: "Not specified"}
        Funding Stage: ${founder.fundingStage ?: "Not specified"}
        Description: ${founder.description ?: "Not specified"}
        """.trimIndent()

        val investorProfile = """
        Name/Firm: ${investor.nameFirm}
        Industry Focus: ${investor.industry ?: "Not specified"}
        Geographic Preference: ${investor.geographicPreference ?: "Not specified"}
        Investment Range: ${investor.investmentRange ?: "Not specified"}
        Funding Stage Preference: ${investor.fundingStagePreference ?: "Not specified"}
        """.trimIndent()

        val aiResult = geminiService.getMatchScore(founderProfile, investorProfile)

        return MatchScore(
            score = aiResult.score,
            reasons = aiResult.reasons.toMutableList()
        )
    }

    // ✅ NEW: AI-powered scoring for investors
    private fun calculateInvestorScoreWithAI(founder: Founder, investor: Investor): MatchScore {
        val founderProfile = """
        Startup: ${founder.startupName}
        Industry: ${founder.industry ?: "Not specified"}
        Location: ${founder.location ?: "Not specified"}
        Team Size: ${founder.teamSize ?: "Not specified"}
        Funding Stage: ${founder.fundingStage ?: "Not specified"}
        Description: ${founder.description ?: "Not specified"}
        """.trimIndent()

        val investorProfile = """
        Name/Firm: ${investor.nameFirm}
        Industry Focus: ${investor.industry ?: "Not specified"}
        Geographic Preference: ${investor.geographicPreference ?: "Not specified"}
        Investment Range: ${investor.investmentRange ?: "Not specified"}
        Funding Stage Preference: ${investor.fundingStagePreference ?: "Not specified"}
        """.trimIndent()

        val aiResult = geminiService.getMatchScore(founderProfile, investorProfile)

        return MatchScore(
            score = aiResult.score,
            reasons = aiResult.reasons.toMutableList()
        )
    }

    private fun Founder.hasRequiredFields(): Boolean {
        return startupName.isNotBlank() &&
                !industry.isNullOrBlank() &&
                !fundingStage.isNullOrBlank()
    }

    private fun Investor.hasRequiredFields(): Boolean {
        return nameFirm.isNotBlank() &&
                !industry.isNullOrBlank() &&
                !fundingStagePreference.isNullOrBlank()
    }
}