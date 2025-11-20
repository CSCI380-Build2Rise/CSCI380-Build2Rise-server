package com.build2rise.backend.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(
    name = "projects",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["founder_id", "investor_id"])
    ]
)
data class Project(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    @Column(name = "founder_id", nullable = false)
    val founderId: UUID,

    @Column(name = "investor_id", nullable = false)
    val investorId: UUID,

    @Column(nullable = false)
    val status: String = "interested", // interested, supporting, funded, declined

    @Column(columnDefinition = "TEXT")
    val notes: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now()
)