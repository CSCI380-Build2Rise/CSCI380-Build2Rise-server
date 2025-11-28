package com.build2rise.backend.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "post_shares")
data class PostShare(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    val post: Post,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(name = "created_at")
    val createdAt: Instant = Instant.now()
)