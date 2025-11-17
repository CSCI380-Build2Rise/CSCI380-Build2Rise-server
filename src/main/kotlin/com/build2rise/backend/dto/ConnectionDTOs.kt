package com.build2rise.backend.dto

// Connection Request
data class ConnectionRequest(
    val targetUserId: String
)

// Connection Response
data class ConnectionResponse(
    val id: String,
    val user1: ConnectionUserInfo,
    val user2: ConnectionUserInfo,
    val status: String, // pending, accepted, rejected
    val connectionDate: String
)

data class ConnectionUserInfo(
    val userId: String,
    val firstName: String?,
    val lastName: String?,
    val userType: String,
    val profileImageUrl: String?
)

// List of Connections Response
data class ConnectionsListResponse(
    val connections: List<ConnectionResponse>,
    val totalCount: Int
)

// Accept/Reject Connection Request
data class UpdateConnectionRequest(
    val status: String // "accepted" or "rejected"
)