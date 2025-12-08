package com.build2rise.backend.service

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

@Service
class GeminiMatchingService(
    @Value("\${gemini.api.key}") private val apiKey: String
) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()
    private val cache = ConcurrentHashMap<String, GeminiMatchResult>()

    fun getMatchScore(founderProfile: String, investorProfile: String): GeminiMatchResult {
        val cacheKey = "${founderProfile.hashCode()}-${investorProfile.hashCode()}"

        cache[cacheKey]?.let { return it }

        val prompt = buildPrompt(founderProfile, investorProfile)
        val result = callGeminiAPI(prompt)

        cache[cacheKey] = result
        return result
    }

    private fun buildPrompt(founderProfile: String, investorProfile: String): String {
        return """
        You are an expert startup-investor matching system for Build2Rise platform.
        
        INVESTOR PROFILE:
        $investorProfile
        
        FOUNDER PROFILE:
        $founderProfile
        
        Analyze compatibility and respond ONLY with valid JSON (no markdown, no code blocks):
        {
          "score": <number 0-100>,
          "reasons": ["<reason1>", "<reason2>", "<reason3>"]
        }
        
        Score guidelines:
        - 80-100: Excellent match (same industry, stage, location)
        - 60-79: Good match (2 out of 3 align)
        - 40-59: Moderate match (1 major alignment)
        - 0-39: Poor match
        
        Keep reasons brief and specific.
        """.trimIndent()
    }

    private fun callGeminiAPI(prompt: String): GeminiMatchResult {
        try {
            val requestBody = """
        {
          "contents": [{
            "parts": [{
              "text": ${gson.toJson(prompt)}
            }]
          }],
          "generationConfig": {
            "temperature": 0.2,
            "topK": 1,
            "topP": 1,
            "maxOutputTokens": 300
          }
        }
        """.trimIndent()

            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$apiKey")
                .post(requestBody.toRequestBody("application/json".toMediaType()))
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errorBody = response.body?.string()
                    println("Gemini API Error: ${response.code} - $errorBody")
                    return getFallbackResult()
                }

                val responseBody = response.body?.string()
                    ?: return getFallbackResult()

                println("Gemini Response: $responseBody")

                val geminiResponse = gson.fromJson(responseBody, GeminiResponse::class.java)
                val text = geminiResponse.candidates[0].content.parts[0].text

                val cleanedText = text
                    .replace("```json", "")
                    .replace("```", "")
                    .trim()

                println("Cleaned JSON: $cleanedText")

                return gson.fromJson(cleanedText, GeminiMatchResult::class.java)
            }
        } catch (e: Exception) {
            println("Gemini API Exception: ${e.message}")
            e.printStackTrace()
            return getFallbackResult()
        }
    }



    private fun getFallbackResult(): GeminiMatchResult {
        return GeminiMatchResult(
            score = 50,
            reasons = listOf("AI matching temporarily unavailable - using basic compatibility")
        )
    }
}

// Gemini API Response Models
data class GeminiResponse(
    val candidates: List<GeminiCandidate>
)

data class GeminiCandidate(
    val content: GeminiContent
)

data class GeminiContent(
    val parts: List<GeminiPart>
)

data class GeminiPart(
    val text: String
)

data class GeminiMatchResult(
    val score: Int,
    val reasons: List<String>
)