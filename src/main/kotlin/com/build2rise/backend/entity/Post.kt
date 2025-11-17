package com.build2rise.backend.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "posts")
data class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(name = "post_description", columnDefinition = "TEXT")
    val postDescription: String? = null,

    @Column(name = "post_type")
    val postType: String = "text",

    @Column(name = "media_url")
    val mediaUrl: String? = null,

    @Column(name = "posting_date", nullable = false)
    val postingDate: LocalDateTime = LocalDateTime.now(),

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)