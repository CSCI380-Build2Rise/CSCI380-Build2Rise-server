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
    private val connectionRepository: ConnectionRepository
) {

    /**
     * Find top matches for an investor (returns founders they haven't supported yet)
     */
    fun findMatchesForInvestor(investorId: UUID, limit: Int = 20): MatchResult {
        val investor = investorRepository.findById(investorId)
            .orElseThrow { IllegalArgumentException("Investor not found") }

        // Get list of founder IDs this investor already supports
        val supportedFounderIds = projectRepository.findByInvestorId(investorId)
            .map { it.founderId }
            .toSet()

        // Get all founders and filter out already-supported ones
        val allFounders = founderRepository.findAll()
            .filter { it.hasRequiredFields() }
            .filter { it.id !in supportedFounderIds }

        val scoredMatches = allFounders
            .map { founder ->
                val score = calculateFounderScore(investor, founder)
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

        // Get list of investor user IDs this founder already has connections with
        val connectedInvestorUserIds = connectionRepository.findByUser1IdOrUser2Id(founder.userId, founder.userId)
            .map { connection ->
                if (connection.user1Id == founder.userId) connection.user2Id else connection.user1Id
            }
            .toSet()

        // Get all investors and filter out already-connected ones
        val allInvestors = investorRepository.findAll()
            .filter { it.hasRequiredFields() }
            .filter { it.userId !in connectedInvestorUserIds }

        val scoredMatches = allInvestors
            .map { investor ->
                val score = calculateInvestorScore(founder, investor)
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

    /**
     * Calculate match score for a founder from investor's perspective
     */
    private fun calculateFounderScore(investor: Investor, founder: Founder): MatchScore {
        val score = MatchScore(0)

        // Industry match (40 points max)
        val investorIndustry = investor.industry?.lowercase()?.trim() ?: ""
        val founderIndustry = founder.industry?.lowercase()?.trim() ?: ""

        if (investorIndustry.isNotEmpty() && founderIndustry.isNotEmpty()) {
            when {
                investorIndustry == founderIndustry -> {
                    score.score += 40
                    score.reasons.add("Same industry: ${founder.industry}")
                }
                isRelatedIndustry(investorIndustry, founderIndustry) -> {
                    score.score += 20
                    score.reasons.add("Related industry: ${founder.industry}")
                }
            }
        }

        // Funding stage match (30-35 points)
        val investorStage = investor.fundingStagePreference?.lowercase()?.trim() ?: ""
        val founderStage = founder.fundingStage?.lowercase()?.trim() ?: ""

        if (investorStage.isNotEmpty() && founderStage.isNotEmpty()) {
            when {
                investorStage == founderStage -> {
                    score.score += 35
                    score.reasons.add("Matches funding stage: ${founder.fundingStage}")
                }
                isRelatedStage(investorStage, founderStage) -> {
                    score.score += 30
                    score.reasons.add("Close funding stage: ${founder.fundingStage}")
                }
            }
        }

        // Location match (20-25 points)
        val investorLocation = investor.geographicPreference?.trim() ?: ""
        val founderLocation = founder.location?.trim() ?: ""

        if (investorLocation.isNotEmpty() && founderLocation.isNotEmpty()) {
            if (isLocationMatch(investorLocation, founderLocation)) {
                score.score += 25
                score.reasons.add("Location match: ${founder.location}")
            } else if (isBroadLocationMatch(investorLocation, founderLocation)) {
                score.score += 20
                score.reasons.add("Region match: ${founder.location}")
            }
        }

        // Team size bonus (10 points)
        val teamSize = founder.teamSize?.lowercase()?.trim() ?: ""
        if (teamSize.isNotEmpty() && teamSize != "solo" && teamSize != "1") {
            score.score += 10
            score.reasons.add("Has a team")
        }

        return score
    }

    /**
     * Calculate match score for an investor from founder's perspective
     */
    private fun calculateInvestorScore(founder: Founder, investor: Investor): MatchScore {
        val score = MatchScore(0)

        // Industry match (40 points max)
        val founderIndustry = founder.industry?.lowercase()?.trim() ?: ""
        val investorIndustry = investor.industry?.lowercase()?.trim() ?: ""

        if (founderIndustry.isNotEmpty() && investorIndustry.isNotEmpty()) {
            when {
                founderIndustry == investorIndustry -> {
                    score.score += 40
                    score.reasons.add("Same industry: ${investor.industry}")
                }
                isRelatedIndustry(founderIndustry, investorIndustry) -> {
                    score.score += 20
                    score.reasons.add("Related industry: ${investor.industry}")
                }
            }
        }

        // Funding stage match (30-35 points)
        val founderStage = founder.fundingStage?.lowercase()?.trim() ?: ""
        val investorStage = investor.fundingStagePreference?.lowercase()?.trim() ?: ""

        if (founderStage.isNotEmpty() && investorStage.isNotEmpty()) {
            when {
                founderStage == investorStage -> {
                    score.score += 35
                    score.reasons.add("Matches your stage: ${investor.fundingStagePreference}")
                }
                isRelatedStage(founderStage, investorStage) -> {
                    score.score += 30
                    score.reasons.add("Close to your stage: ${investor.fundingStagePreference}")
                }
            }
        }

        // Location match (20-25 points)
        val founderLocation = founder.location?.trim() ?: ""
        val investorLocation = investor.geographicPreference?.trim() ?: ""

        if (founderLocation.isNotEmpty() && investorLocation.isNotEmpty()) {
            if (isLocationMatch(investorLocation, founderLocation)) {
                score.score += 25
                score.reasons.add("Geographic match: ${investor.geographicPreference}")
            } else if (isBroadLocationMatch(investorLocation, founderLocation)) {
                score.score += 20
                score.reasons.add("Regional match: ${investor.geographicPreference}")
            }
        }

        return score
    }

    private fun isRelatedIndustry(industry1: String, industry2: String): Boolean {
        val relatedIndustries = mapOf(
            "technology" to listOf("software", "ai", "ml", "ai/ml", "saas", "fintech", "edtech", "healthtech"),
            "healthcare" to listOf("medtech", "biotech", "health tech", "pharma"),
            "finance" to listOf("fintech", "banking", "insurance", "payments"),
            "cleantech" to listOf("renewable energy", "sustainability", "green tech", "climate tech"),
            "ai/ml" to listOf("technology", "software", "ai", "ml", "machine learning"),
            "saas" to listOf("technology", "software", "b2b", "enterprise"),
            "fintech" to listOf("finance", "banking", "payments", "cryptocurrency")
        )

        return relatedIndustries[industry1]?.contains(industry2) == true ||
                relatedIndustries[industry2]?.contains(industry1) == true
    }

    private fun isRelatedStage(stage1: String, stage2: String): Boolean {
        val stageOrder = listOf(
            "idea/pre-seed", "idea", "pre-seed",
            "seed",
            "series a",
            "series b",
            "growth stage", "late stage"
        )

        val index1 = stageOrder.indexOfFirst { it.contains(stage1) || stage1.contains(it) }
        val index2 = stageOrder.indexOfFirst { it.contains(stage2) || stage2.contains(it) }

        if (index1 == -1 || index2 == -1) return false

        return kotlin.math.abs(index1 - index2) == 1
    }

    private fun isLocationMatch(investorLocation: String, founderLocation: String): Boolean {
        val cleanInvestorLocation = investorLocation.replace(Regex("^[\\p{So}\\p{Sk}]+ "), "").lowercase().trim()
        val cleanFounderLocation = founderLocation.replace(Regex("^[\\p{So}\\p{Sk}]+ "), "").lowercase().trim()

        if (cleanInvestorLocation in listOf("global", "remote-friendly", "remote", "anywhere")) {
            return true
        }

        if (cleanInvestorLocation == cleanFounderLocation) {
            return true
        }

        if (cleanFounderLocation.contains(cleanInvestorLocation)) {
            return true
        }

        return false
    }

    private fun isBroadLocationMatch(investorLocation: String, founderLocation: String): Boolean {
        val cleanInvestorLocation = investorLocation.replace(Regex("^[\\p{So}\\p{Sk}]+ "), "").lowercase().trim()
        val cleanFounderLocation = founderLocation.replace(Regex("^[\\p{So}\\p{Sk}]+ "), "").lowercase().trim()

        val regionMap = mapOf(
            "north america" to listOf("united states", "usa", "us", "canada", "new york", "san francisco", "boston", "austin", "seattle", "los angeles", "toronto", "vancouver"),
            "united states" to listOf("new york", "san francisco", "boston", "austin", "seattle", "los angeles", "silicon valley"),
            "europe" to listOf("london", "berlin", "amsterdam", "paris", "barcelona", "stockholm", "uk", "germany", "france", "netherlands", "spain", "sweden", "united kingdom"),
            "asia" to listOf("singapore", "tokyo", "bangalore", "hong kong", "seoul", "beijing", "shanghai", "india", "japan", "china"),
            "oceania" to listOf("sydney", "melbourne", "auckland", "australia", "new zealand"),
            "latin america" to listOf("são paulo", "mexico city", "buenos aires", "brazil", "mexico", "argentina"),
            "middle east" to listOf("dubai", "tel aviv", "uae", "israel"),
            "africa" to listOf("cape town", "lagos", "nairobi", "south africa", "nigeria", "kenya")
        )

        regionMap[cleanInvestorLocation]?.forEach { city ->
            if (cleanFounderLocation.contains(city)) {
                return true
            }
        }

        return false
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