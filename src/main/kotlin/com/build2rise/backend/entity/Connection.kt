package com.build2rise.backend.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "connections")
data class Connection(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    @Column(name = "user1_id", nullable = false)
    val user1Id: UUID,

    @Column(name = "user2_id", nullable = false)
    val user2Id: UUID,

    @Column(nullable = false)
    val status: String = "pending", // pending, accepted, rejected

    @Column(name = "connection_date", nullable = false)
    val connectionDate: LocalDateTime = LocalDateTime.now(),

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
