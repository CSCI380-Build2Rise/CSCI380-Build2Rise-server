package com.build2rise.backend.controller

import com.build2rise.backend.dto.*
import com.build2rise.backend.service.ConnectionService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/connections")
@CrossOrigin(origins = ["*"])
class ConnectionController(
    private val connectionService: ConnectionService
) {

    /**
     * Request a connection
     * POST /api/connections/request
     */
    @PostMapping("/request")
    fun requestConnection(
        authentication: Authentication,
        @Valid @RequestBody request: ConnectionRequest
    ): ResponseEntity<ConnectionResponse> {
        return try {
            val userId = authentication.principal as String
            val connection = connectionService.requestConnection(userId, request)
            ResponseEntity.status(HttpStatus.CREATED).body(connection)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }


    /**
     * Get all connections for current user
     * GET /api/connections
     */
//    @GetMapping
//    fun getUserConnections(authentication: Authentication): ResponseEntity<ConnectionsListResponse> {
//        val userId = authentication.principal as String
//        val connections = connectionService.getUserConnections(userId)
//        return ResponseEntity.ok(connections)
//    }
    /**
     * Get connections for current user with optional status filter
     * GET /api/connections?status=accepted
     * GET /api/connections?status=pending
     * GET /api/connections (returns all)
     */
    @GetMapping
    fun getUserConnections(
        authentication: Authentication,
        @RequestParam(required = false) status: String?
    ): ResponseEntity<ConnectionsListResponse> {
        val userId = authentication.principal as String
        val connections = connectionService.getUserConnections(userId, status)
        return ResponseEntity.ok(connections)
    }
    /**
     * Accept or reject connection
     * PUT /api/connections/{connectionId}
     */
    @PutMapping("/{connectionId}")
    fun updateConnectionStatus(
        authentication: Authentication,
        @PathVariable connectionId: String,
        @Valid @RequestBody request: UpdateConnectionRequest
    ): ResponseEntity<ConnectionResponse> {
        return try {
            val userId = authentication.principal as String
            val connection = connectionService.updateConnectionStatus(connectionId, userId, request)
            ResponseEntity.ok(connection)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }
}