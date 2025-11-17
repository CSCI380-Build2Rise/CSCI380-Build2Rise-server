package com.build2rise.backend.service

import com.build2rise.backend.dto.*
import com.build2rise.backend.entity.Connection
import com.build2rise.backend.repository.ConnectionRepository
import com.build2rise.backend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ConnectionService(
    private val connectionRepository: ConnectionRepository,
    private val userRepository: UserRepository
) {

    /**
     * Request a connection
     */
    @Transactional
    fun requestConnection(userId: String, request: ConnectionRequest): ConnectionResponse {
        val userUuid = UUID.fromString(userId)
        val targetUuid = UUID.fromString(request.targetUserId)

        // Verify both users exist
        val user = userRepository.findById(userUuid).orElseThrow {
            IllegalArgumentException("User not found")
        }

        val targetUser = userRepository.findById(targetUuid).orElseThrow {
            IllegalArgumentException("Target user not found")
        }

        // Create connection
        val connection = Connection(
            user1Id = userUuid,
            user2Id = targetUuid,
            status = "pending"
        )

        val savedConnection = connectionRepository.save(connection)

        return ConnectionResponse(
            id = savedConnection.id.toString(),
            user1 = ConnectionUserInfo(
                userId = user.id.toString(),
                firstName = user.firstName,
                lastName = user.lastName,
                userType = user.userType,
                profileImageUrl = user.profileImageUrl
            ),
            user2 = ConnectionUserInfo(
                userId = targetUser.id.toString(),
                firstName = targetUser.firstName,
                lastName = targetUser.lastName,
                userType = targetUser.userType,
                profileImageUrl = targetUser.profileImageUrl
            ),
            status = savedConnection.status,
            connectionDate = savedConnection.connectionDate.toString()
        )
    }

    /**
     * Get all connections for a user
     */
    fun getUserConnections(userId: String): ConnectionsListResponse {
        val userUuid = UUID.fromString(userId)

        val connections = connectionRepository.findAcceptedConnections(userUuid)

        val connectionResponses = connections.map { connection ->
            val user1 = userRepository.findById(connection.user1Id).orElse(null)
            val user2 = userRepository.findById(connection.user2Id).orElse(null)

            ConnectionResponse(
                id = connection.id.toString(),
                user1 = ConnectionUserInfo(
                    userId = connection.user1Id.toString(),
                    firstName = user1?.firstName,
                    lastName = user1?.lastName,
                    userType = user1?.userType ?: "unknown",
                    profileImageUrl = user1?.profileImageUrl
                ),
                user2 = ConnectionUserInfo(
                    userId = connection.user2Id.toString(),
                    firstName = user2?.firstName,
                    lastName = user2?.lastName,
                    userType = user2?.userType ?: "unknown",
                    profileImageUrl = user2?.profileImageUrl
                ),
                status = connection.status,
                connectionDate = connection.connectionDate.toString()
            )
        }

        return ConnectionsListResponse(
            connections = connectionResponses,
            totalCount = connectionResponses.size
        )
    }

    /**
     * Accept or reject connection
     */
    @Transactional
    fun updateConnectionStatus(
        connectionId: String,
        userId: String,
        request: UpdateConnectionRequest
    ): ConnectionResponse {
        val connectionUuid = UUID.fromString(connectionId)
        val userUuid = UUID.fromString(userId)

        val connection = connectionRepository.findById(connectionUuid).orElseThrow {
            IllegalArgumentException("Connection not found")
        }

        // Verify user is the receiver (user2)
        if (connection.user2Id != userUuid) {
            throw IllegalArgumentException("Not authorized to update this connection")
        }

        // Update status
        val updatedConnection = connection.copy(
            id = connection.id,
            user1Id = connection.user1Id,
            user2Id = connection.user2Id,
            status = request.status,
            connectionDate = connection.connectionDate,
            createdAt = connection.createdAt
        )

        val savedConnection = connectionRepository.save(updatedConnection)

        val user1 = userRepository.findById(savedConnection.user1Id).orElse(null)
        val user2 = userRepository.findById(savedConnection.user2Id).orElse(null)

        return ConnectionResponse(
            id = savedConnection.id.toString(),
            user1 = ConnectionUserInfo(
                userId = savedConnection.user1Id.toString(),
                firstName = user1?.firstName,
                lastName = user1?.lastName,
                userType = user1?.userType ?: "unknown",
                profileImageUrl = user1?.profileImageUrl
            ),
            user2 = ConnectionUserInfo(
                userId = savedConnection.user2Id.toString(),
                firstName = user2?.firstName,
                lastName = user2?.lastName,
                userType = user2?.userType ?: "unknown",
                profileImageUrl = user2?.profileImageUrl
            ),
            status = savedConnection.status,
            connectionDate = savedConnection.connectionDate.toString()
        )
    }
}