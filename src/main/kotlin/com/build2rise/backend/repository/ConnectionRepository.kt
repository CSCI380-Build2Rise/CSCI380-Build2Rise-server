package com.build2rise.backend.repository

import com.build2rise.backend.entity.Connection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ConnectionRepository : JpaRepository<Connection, UUID> {

    @Query("""
        SELECT c FROM Connection c 
        WHERE (c.user1Id = :userId OR c.user2Id = :userId)
        AND c.status = 'accepted'
    """)
    fun findAcceptedConnections(userId: UUID): List<Connection>

    @Query("""
        SELECT c FROM Connection c 
        WHERE (c.user1Id = :userId OR c.user2Id = :userId)
        AND c.status = 'pending'
    """)
    fun findPendingConnections(userId: UUID): List<Connection>
}