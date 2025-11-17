package com.build2rise.backend.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "founders")
data class Founder(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    @Column(name = "user_id", unique = true, nullable = false)
    val userId: UUID,

    @Column(name = "startup_name", nullable = false)
    val startupName: String,

    val industry: String? = null,

    val location: String? = null,

    @Column(name = "team_size")
    val teamSize: String? = null,

    @Column(name = "funding_stage")
    val fundingStage: String? = null,

    @Column(columnDefinition = "TEXT")
    val description: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)