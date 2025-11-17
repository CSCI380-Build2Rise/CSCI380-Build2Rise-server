package com.build2rise.backend.repository

import com.build2rise.backend.entity.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MessageRepository : JpaRepository<Message, UUID> {
    @Query("""
        SELECT m FROM Message m 
        WHERE (m.senderId = :userId OR m.receiverId = :userId)
        ORDER BY m.timestamp DESC
    """)
    fun findConversations(userId: UUID): List<Message>

    @Query("""
        SELECT m FROM Message m 
        WHERE (m.senderId = :user1Id AND m.receiverId = :user2Id)
           OR (m.senderId = :user2Id AND m.receiverId = :user1Id)
        ORDER BY m.timestamp ASC
    """)
    fun findConversationBetween(user1Id: UUID, user2Id: UUID): List<Message>
}