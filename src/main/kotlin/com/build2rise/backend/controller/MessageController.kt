package com.build2rise.backend.controller

import com.build2rise.backend.dto.*
import com.build2rise.backend.service.MessageService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = ["*"])
class MessageController(
    private val messageService: MessageService
) {

    /**
     * Send a message
     * POST /api/messages/send
     */
    @PostMapping("/send")
    fun sendMessage(
        authentication: Authentication,
        @Valid @RequestBody request: SendMessageRequest
    ): ResponseEntity<MessageResponse> {
        return try {
            val senderId = authentication.principal as String
            val message = messageService.sendMessage(senderId, request)
            ResponseEntity.status(HttpStatus.CREATED).body(message)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    /**
     * Get all conversations for current user
     * GET /api/messages/conversations
     */
    @GetMapping("/conversations")
    fun getUserConversations(authentication: Authentication): ResponseEntity<List<ConversationResponse>> {
        val userId = authentication.principal as String
        val conversations = messageService.getUserConversations(userId)
        return ResponseEntity.ok(conversations)
    }

    /**
     * Get conversation with specific user
     * GET /api/messages/conversation/{otherUserId}
     */
    @GetMapping("/conversation/{otherUserId}")
    fun getConversation(
        authentication: Authentication,
        @PathVariable otherUserId: String
    ): ResponseEntity<ConversationDetailResponse> {
        return try {
            val userId = authentication.principal as String
            val conversation = messageService.getConversation(userId, otherUserId)
            ResponseEntity.ok(conversation)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}