package com.build2rise.backend.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "investors")
data class Investor(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "user_id", unique = true, nullable = false)
    val userId: UUID,

    @Column(name = "name_firm", nullable = false)
    val nameFirm: String,

    val industry: String? = null,

    @Column(name = "geographic_preference")
    val geographicPreference: String? = null,

    @Column(name = "investment_range")
    val investmentRange: String? = null,

    @Column(name = "funding_stage_preference")
    val fundingStagePreference: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)