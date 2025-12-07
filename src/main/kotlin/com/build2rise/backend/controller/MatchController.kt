package com.build2rise.backend.controller

import com.build2rise.backend.dto.MatchResult
import com.build2rise.backend.service.MatchingService
import com.build2rise.backend.repository.InvestorRepository
import com.build2rise.backend.repository.FounderRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/matches")
class MatchController(
    private val matchingService: MatchingService,
    private val investorRepository: InvestorRepository,
    private val founderRepository: FounderRepository
) {

    @GetMapping("/for-investor/{investorId}")
    fun getMatchesForInvestor(
        @PathVariable investorId: UUID,
        @RequestParam(defaultValue = "20") limit: Int
    ): ResponseEntity<MatchResult> {
        return try {
            val matches = matchingService.findMatchesForInvestor(investorId, limit)
            ResponseEntity.ok(matches)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/for-founder/{founderId}")
    fun getMatchesForFounder(
        @PathVariable founderId: UUID,
        @RequestParam(defaultValue = "20") limit: Int
    ): ResponseEntity<MatchResult> {
        return try {
            val matches = matchingService.findMatchesForFounder(founderId, limit)
            ResponseEntity.ok(matches)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/for-current-user")
    fun getMatchesForCurrentUser(
        @RequestParam(defaultValue = "20") limit: Int,
        authentication: Authentication
    ): ResponseEntity<MatchResult> {
        return try {
            // Get user ID from JWT token
            val userId = UUID.fromString(authentication.name)

            // Try to find investor first
            val investor = investorRepository.findByUserId(userId)
            if (investor != null) {
                val matches = matchingService.findMatchesForInvestor(investor.id!!, limit)
                return ResponseEntity.ok(matches)
            }

            // Try to find founder
            val founder = founderRepository.findByUserId(userId)
            if (founder != null) {
                val matches = matchingService.findMatchesForFounder(founder.id!!, limit)
                return ResponseEntity.ok(matches)
            }

            // No profile found
            ResponseEntity.status(404).body(
                MatchResult(emptyList(), 0, 0.0)
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }
}