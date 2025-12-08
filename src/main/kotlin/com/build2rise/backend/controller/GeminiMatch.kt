package com.build2rise.backend.controller



import com.build2rise.backend.service.GeminiMatchingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/test")
class TestController(
    private val geminiService: GeminiMatchingService
) {

    @GetMapping("/gemini")
    fun testGemini(): ResponseEntity<Any> {
        val founderProfile = """
        Startup: EcoTrack
        Industry: Sustainability
        Description: AI-powered carbon footprint tracking for small businesses
        Location: San Francisco
        Team Size: 3
        Funding Stage: Seed
        """.trimIndent()

        val investorProfile = """
        Name/Firm: Green Ventures
        Industry: Sustainability
        Funding Stage Preference: Seed
        Geographic Preference: North America
        Investment Range: $100K-$500K
        """.trimIndent()

        return try {
            val result = geminiService.getMatchScore(founderProfile, investorProfile)
            ResponseEntity.ok(mapOf(
                "success" to true,
                "result" to result
            ))
        } catch (e: Exception) {
            ResponseEntity.ok(mapOf(
                "success" to false,
                "error" to e.message
            ))
        }
    }
}